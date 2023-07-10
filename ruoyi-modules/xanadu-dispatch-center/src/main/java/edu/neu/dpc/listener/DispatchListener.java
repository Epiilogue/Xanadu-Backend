package edu.neu.dpc.listener;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.*;
import edu.neu.dpc.entity.Dispatch;
import edu.neu.dpc.entity.Product;
import edu.neu.dpc.entity.Task;
import edu.neu.dpc.feign.CCOrderClient;
import edu.neu.dpc.feign.CenterWareClient;
import edu.neu.dpc.feign.SubstationClient;
import edu.neu.dpc.feign.SubwareClient;
import edu.neu.dpc.service.DispatchService;
import edu.neu.dpc.service.ProductService;
import edu.neu.dpc.service.TaskService;
import edu.neu.dpc.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Slf4j
@RocketMQMessageListener(topic = MQTopic.ORDER_TOPIC,consumerGroup = "order-service-consumer-group")
public class DispatchListener implements RocketMQListener<DispatchMessage>{

    private static final String WARE_KEY = "wareLocation";

    private static final Double averageSpeedInKph = 40.0;

    @Autowired
    DispatchService dispatchService;

    @Autowired
    CCOrderClient ccOrderClient;

    @Autowired
    TaskService taskService;

    @Autowired
    ProductService productService;

    @Autowired
    CenterWareClient centerWareClient;

    @Autowired
    SubstationClient substationClient;

    @Autowired
    SubwareClient subwareClient;

    @Autowired
    HashMap<Long, Date> map;

    @Autowired
    @SuppressWarnings("all")
    RedisTemplate redisTemplate;

    @Override
    @Transactional
    @SuppressWarnings("all")
    public void onMessage(DispatchMessage dispatchMessage) {
        //拿到调度的订单id号以及目标分站id，我们还是需要查询一下订单是否被撤销，因为客户可能会撤销订单
        System.out.println(dispatchMessage);
        Long id = dispatchMessage.getOrderId();
        Long substationId = dispatchMessage.getSubstationId();
        try {

            AjaxResult orderResult = ccOrderClient.getOrder(id);
            if (orderResult.isError()) throw new ServiceException("获取订单信息失败");
            Object data = orderResult.get("data");
            OrderVo orderVo = JSON.parseObject(JSON.toJSONString(data), OrderVo.class);
            if (!orderVo.getStatus().equals(OrderStatusConstant.CAN_BE_ALLOCATED)) return;//订单状态不是可分配状态，直接返回
            String taskType = taskService.resolveTaskType(orderVo);
            if (taskType == null) throw new ServiceException("无法解析任务类型");
            AjaxResult remoteSubwareResult = substationClient.getSubwareId(substationId);
            if (remoteSubwareResult == null || remoteSubwareResult.isError())
                throw new ServiceException("获取分库ID失败");
            Long subwareId = (Long) remoteSubwareResult.get("data");
            Task task = new Task(null, orderVo.getId(), substationId, TaskStatus.SCHEDULED
                    , false, taskType);
            boolean success = taskService.save(task);
            if (!success) throw new ServiceException("保存任务失败");
            Long taskId = task.getId();
            Boolean isRemoteSuccess = ccOrderClient.batchUpdateStatus(OrderStatusConstant.DISPATCHED, Collections.singletonList(orderVo.getId()));
            if (isRemoteSuccess == null || !isRemoteSuccess) throw new ServiceException("更新订单状态失败");

            //此时需要计算预计出库时间，可以利用仓库的地理位置以及中心仓库的地理位置进行自动估算
            //这里我们可以利用redis的GEO命令，将分站的地理位置和中心仓库的地理位置存储到redis中，然后利用GEO命令计算距离
            //然后根据距离计算出预计出库时间，加上当前的时间得到最终时间
            AjaxResult subwareInfo = subwareClient.info(subwareId);
            if (subwareInfo == null || subwareInfo.isError()) throw new ServiceException("获取分库信息失败");
            SubwareVo subwareVo = JSON.parseObject(JSON.toJSONString(subwareInfo.get("data")), SubwareVo.class);
            RedisGeoCommands.GeoLocation<String> subwareLocation = new RedisGeoCommands.GeoLocation<>(subwareVo.getName(), new Point(subwareVo.getX(), subwareVo.getY()));

            AjaxResult centerWareInfo = centerWareClient.info();
            if (centerWareInfo == null || centerWareInfo.isError()) throw new ServiceException("获取中心仓库信息失败");
            CenterwareVo centerWareVo = JSON.parseObject(JSON.toJSONString(centerWareInfo.get("data")), CenterwareVo.class);
            RedisGeoCommands.GeoLocation<String> centerWareLocation = new RedisGeoCommands.GeoLocation<>(centerWareVo.getName(), new Point(centerWareVo.getX(), centerWareVo.getY()));

            redisTemplate.opsForGeo().add(WARE_KEY, subwareLocation);
            redisTemplate.opsForGeo().add(WARE_KEY, centerWareLocation);

            Distance geo_distance = redisTemplate.opsForGeo().distance(WARE_KEY, centerWareVo.getName(), subwareVo.getName(), RedisGeoCommands.DistanceUnit.KILOMETERS);
            double distanceValue;
            if (geo_distance != null) {
                distanceValue = geo_distance.getValue();
            } else {
                throw new ServiceException("计算距离失败");
            }
            //计算出预计出库时间
            Double hours = distanceValue / averageSpeedInKph;
            //我们拿到了小时，我们需要将小时转换为毫秒，然后加上当前时间，得到最终的预计出库时间
            Long outDateTimeStamp = System.currentTimeMillis() + (long) (hours * 60 * 60 * 1000);
            Date outDate = new Date(outDateTimeStamp);
            //如果是收款任务，我们需要保存一下预计出库日期，在后续创建送货单时使用
            if (taskType.equals(TaskType.PAYMENT)) map.put(id, outDate);
            // 1.收款任务，不需要记录商品列表，直接状态为可分配，不调度
            if (!taskType.equals(TaskType.PAYMENT)) {
                List<Product> products = orderVo.getProducts();
                products.forEach(p -> p.setTaskId(taskId));
                success = productService.saveBatch(products);
                if (!success) throw new ServiceException("保存商品失败");
                if (!taskType.equals(TaskType.RETURN))
                    products.forEach(p -> {
                        //修改库存，将对应的商品库存的加锁量减去商品数量，增加已分配量
                        AjaxResult lock = centerWareClient.dispatch(p.getProductId(), p.getNumber(), "lock");
                        if (lock == null || lock.isError()) throw new ServiceException("解锁库存失败");
                        //生成调度单并插入，状态为已提交
                        Dispatch dispatch = new Dispatch(null, subwareId, taskId, p.getProductId(), p.getNumber(), p.getProductName(),
                                p.getProductCategary(), outDate, Dispatch.NOT_OUTPUT, substationId, false);
                        boolean result = dispatchService.save(dispatch);
                        if (!result) throw new ServiceException("保存调度单失败");
                        //远程调用添加出库单，中心仓库添加两个不同的查询页面，对应不同的vo
                        CenterOutputVo centerOutputVo = new CenterOutputVo(dispatch.getId(), dispatch.getTaskId(), dispatch.getProductId(),
                                dispatch.getProductName(), p.getPrice(), dispatch.getProductNum(), InputOutputType.DISPATCH_OUT, outDate, null, substationId, subwareId);
                        Boolean add = centerWareClient.add(centerOutputVo);
                        if (add == null || !add) throw new ServiceException("添加出库调度记录失败");
                    });
            }
        } catch (ServiceException e) {
            //如果出现异常,自动调度失败
            log.error("调度失败", e);
        }
    }
}
