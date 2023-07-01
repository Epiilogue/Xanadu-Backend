package edu.neu.dpc.controller;


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
import edu.neu.dpc.service.DispatchService;
import edu.neu.dpc.service.ProductService;
import edu.neu.dpc.service.TaskService;
import edu.neu.dpc.vo.CenterOutputVo;
import edu.neu.dpc.vo.DispatchVo;
import edu.neu.dpc.vo.OrderVo;
import edu.neu.dpc.vo.StorageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@RestController
@RequestMapping("/dpc/dispatch")
@Api(value = "调度中心", tags = "调度中心")
@Transactional
@CacheConfig(cacheNames = "dispatch")
public class DispatchController {

    //1. 列出所有的订单，可以复用order接口，获取所有订单
    //2. 点击检查，可以检查订单状态是否从缺货恢复，如果恢复则可以进行分配，并且锁定库存，远程调用实现

    @Autowired
    CCOrderClient ccOrderClient;

    @Autowired
    TaskService taskService;

    @Autowired
    ProductService productService;

    @Autowired
    DispatchService dispatchService;

    @Autowired
    CenterWareClient centerWareClient;

    @Autowired
    SubstationClient substationClient;

    Map<Long, Date> map = new HashMap<>();


    @GetMapping("/check/{id}")
    @ApiOperation(value = "检查订单状态，是否全部都到货，如果都到货则更新为可分配状态，需要锁定库存", notes = "检查订单")
    public AjaxResult checkOrder(@PathVariable("id") Long id) {
        return ccOrderClient.checkAllArrival(id);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("获取调拨单")
    @Cacheable(key = "#id")
    public AjaxResult get(@PathVariable Integer id) {
        Dispatch dispatch = dispatchService.getById(id);
        if (dispatch == null) {
            return AjaxResult.error("调拨单不存在");
        }
        return AjaxResult.success(dispatch);
    }

    /**
     * 任务单： 订单号、任务单、客户姓名、投递地址、商品名称、商品数量、要求完成日期、任务类型、任务状态
     * <p>
     * 商品调拨单： 商品调拨单号、入库库房、商品分类、商品名称、库存量、调拨数量、要求出库日期
     * <p>
     * 还需要考虑是货到付款还是先付款再送货，若是先付款再送货则需要生成的是付款任务单，完成送货后调度为送货任务单
     */


    /**
     * 在这里需要判断任务的类型，如果是退货、收款任务的话不需要进行商品调度，如果是换货、新订任务的话则需要判断,送货任务完成收款任务后创建
     * 需要考虑的是付款送货的情况，该情况较为特殊，需要先生成收款任务，收款任务完成后再生成送货任务以及调度任务，送货失败了直接退款即可，不需要生
     * 成退款任务增加复杂度。
     */
    @PutMapping("/dispatchOrder/{id}/{substationId}")
    @ApiOperation(value = "手动调度订单,传入参数为订单id和分站id", notes = "调度订单")
    public AjaxResult dispatchOrder(@ApiParam("订单ID") @PathVariable("id") Long id,
                                    @ApiParam("子站ID") @PathVariable("substationId") Long substationId,
                                    @ApiParam("预计出库日期") @RequestParam("outDate") Date outDate) {
        //拉取订单信息，生成任务单
        AjaxResult orderResult = ccOrderClient.getOrder(id);
        //检查返回结果是否有错误
        if (orderResult.isError()) return orderResult;
        //获取订单信息
        Object data = orderResult.get("data");
        //转为OrderVo
        OrderVo orderVo = JSON.parseObject(JSON.toJSONString(data), OrderVo.class);
        String taskType = taskService.resolveTaskType(orderVo);
        if (taskType == null) throw new ServiceException("无法解析任务类型");
        AjaxResult remoteSubwareResult = substationClient.getSubwareId(substationId);
        if (remoteSubwareResult == null) throw new ServiceException("获取分库ID失败");
        if (remoteSubwareResult.isError()) return remoteSubwareResult;
        Long subwareId = (Long) remoteSubwareResult.get("data");
        //生成任务单
        Task task = new Task(null, orderVo.getId(), substationId, TaskStatus.SCHEDULED
                , false, taskType);

        //如果是收款任务或者退货任务则直接设置为可分配状态
        if (taskType.equals(TaskType.PAYMENT) || taskType.equals(TaskType.RETURN))
            task.setTaskStatus(TaskStatus.ASSIGNABLE);

        boolean success = taskService.save(task);
        if (!success) throw new ServiceException("保存任务失败");
        // 拿到对应的记录ID
        Long taskId = task.getId();
        //2.更新订单状态为已调度
        Boolean isRemoteSuccess = ccOrderClient.batchUpdateStatus(OrderStatusConstant.DISPATCHED, Collections.singletonList(orderVo.getId()));
        //更新订单分站ID
        ccOrderClient.updateOrderSubstationId(substationId, orderVo.getId());
        if (isRemoteSuccess == null || !isRemoteSuccess) throw new ServiceException("更新订单状态失败");

        //如果是收款任务，我们需要保存一下预计出库日期，在后续创建送货单时使用
        if (taskType.equals(TaskType.PAYMENT)) map.put(id, outDate);

        // 1.收款任务，不需要记录商品列表，直接状态为可分配，不调度
        if (!taskType.equals(TaskType.PAYMENT)) {
            List<Product> products = orderVo.getProducts();
            products.forEach(p -> p.setTaskId(taskId));
            success = productService.saveBatch(products);
            if (!success) throw new ServiceException("保存商品失败");

            // 2.退货任务，记录商品列表，不调度，直接可分配
            if (taskType.equals(TaskType.RETURN)) return AjaxResult.success("调度成功");

            // 3.购买任务，记录列表，调度
            // 4.换货任务，记录列表，调度
            //生成调度出库记录，解锁，添加到已分配区,并生成调度单
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
        return AjaxResult.success("调度成功");
    }


    @PostMapping("/feign/createDeliveryTask")
    @ApiOperation(value = "创建送货任务单,需要调度商品以及保存相关的商品信息")
    public AjaxResult createDeliveryTask(@ApiParam("订单ID") @RequestParam("orderId") Long orderId,
                                         @ApiParam("分站ID") @RequestParam("substationId") Long substationId,
                                         @RequestParam("subwareId") Long subwareId) {
        //拉取订单信息，生成任务单
        AjaxResult orderResult = ccOrderClient.getOrder(orderId);
        //检查返回结果是否有错误
        if (orderResult.isError()) return orderResult;
        //获取订单信息
        Object data = orderResult.get("data");
        //转为OrderVo
        OrderVo orderVo = JSON.parseObject(JSON.toJSONString(data), OrderVo.class);
        //生成任务单
        Task task = new Task(null, orderVo.getId(), substationId, TaskStatus.SCHEDULED
                , false, TaskType.DELIVERY);
        boolean success = taskService.save(task);
        if (!success) throw new ServiceException("生成送货任务失败");
        // 拿到对应的记录ID
        Long taskId = task.getId();
        List<Product> products = orderVo.getProducts();
        products.forEach(p -> p.setTaskId(taskId));
        success = productService.saveBatch(products);
        if (!success) throw new ServiceException("保存商品失败");

        //取出之前保存的预计出库日期
        Date outDate = map.getOrDefault(orderId, new Date());
        map.remove(orderId);

        //保存每一个商品并生成调度记录
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
        return AjaxResult.success("创建送货任务单并调度成功");
    }


    @PutMapping("/dispatchProduct")
    @ApiOperation(value = "调度商品,传入参数为商品id和分库id,要求出库日期", notes = "调度商品")
    public AjaxResult dispatchProduct(@ApiParam("分站ID") @RequestParam("subwareId") Long substationId,
                                      @ApiParam("要求出库日期") @RequestParam("requireDate") Date requireDate,
                                      @ApiParam("商品信息") Product product) {

        AjaxResult remoteSubwareResult = substationClient.getSubwareId(substationId);
        if (remoteSubwareResult == null) throw new ServiceException("获取分库ID失败");
        if (remoteSubwareResult.isError()) return remoteSubwareResult;
        Long subwareId = (Long) remoteSubwareResult.get("data");
        //构造并保存调度单
        Dispatch dispatch = new Dispatch(null, subwareId, null, product.getId(), product.getNumber(), product.getProductName(),
                product.getProductCategary(), requireDate, Dispatch.NOT_OUTPUT, substationId, false);


        boolean success = dispatchService.save(dispatch);
        //尝试修改库存，调度需要从可分配库存中减去对应的数量，添加到已分配库存中，后续从已分配库存中减去对应的数量
        if (!success) throw new RuntimeException("保存调度单失败");
        AjaxResult unlock = centerWareClient.dispatch(product.getId(), product.getNumber(), "unlock");
        if (unlock.isError()) throw new ServiceException("可分配库存不足");

        CenterOutputVo centerOutputVo = new CenterOutputVo(dispatch.getId(), null, dispatch.getProductId(), dispatch.getProductName(), product.getPrice(),
                dispatch.getProductNum(), InputOutputType.DISPATCH_OUT, requireDate, null, substationId, subwareId);
        Boolean add = centerWareClient.add(centerOutputVo);
        if (add == null || !add) throw new ServiceException("添加出库调度记录失败");

        return AjaxResult.success("调度成功");
    }

    @PutMapping("/feign/updateDispatchStatus/{id}")
    @ApiOperation("更新调度单状态为已出库,传入参数为调度单id和调度单信息")
    @CacheEvict(key = "#id")
    public AjaxResult updateDispatchStatus(@ApiParam("调度单ID") @PathVariable("id") Long id) {
        //修改调度单状态为已提交
        Dispatch dispatch = dispatchService.getById(id);
        if (dispatch == null) return AjaxResult.error("调度单不存在");
        //检查状态是否为已提交
        if (!Objects.equals(dispatch.getStatus(), Dispatch.NOT_OUTPUT)) return AjaxResult.error("调度单状态不正确");
        dispatch.setStatus(Dispatch.OUTPUTED);
        if (!dispatchService.updateById(dispatch)) throw new RuntimeException("更新调度单状态失败");
        return AjaxResult.success("提交成功");
    }


    @DeleteMapping("/deleteDispatch/{id}")
    @ApiOperation(value = "删除调度单,传入参数为调度单id", notes = "删除调度单")
    @CacheEvict(key = "#id")
    public AjaxResult deleteDispatch(@ApiParam("调度单ID") @PathVariable("id") Long id) {
        //修改调度单状态为已提交
        Dispatch dispatch = dispatchService.getById(id);
        if (dispatch == null) return AjaxResult.error("调度单不存在");
        //检查状态是否为未提交
        if (dispatch.getTaskId() != null) throw new ServiceException("该调度单已与任务单关联，不允许删除");
        if (!Objects.equals(dispatch.getStatus(), Dispatch.NOT_OUTPUT))
            return AjaxResult.error("商品已出库，不允许删除记录");
        dispatch.setStatus(Dispatch.NOT_OUTPUT);
        //重新回滚库存,删除掉之前的但是增加0
        AjaxResult reDispatchSuccess = centerWareClient.reDispatch(dispatch.getProductId(), dispatch.getProductNum(), 0);
        if (reDispatchSuccess.isError()) throw new ServiceException("修改库存失败: " + reDispatchSuccess.getMsg());
        if (!dispatchService.removeById(dispatch)) throw new ServiceException("更新调度单状态失败");
        Boolean delete = centerWareClient.delete(dispatch.getId());
        if (delete == null || !delete) throw new ServiceException("删除仓库出库调度记录失败");

        return AjaxResult.success("删除成功");
    }


    @PostMapping("/editDispatch")
    @ApiOperation(value = "修改商品调度单, 只允许修改调度的数量以及时间，目的地等信息", notes = "修改商品调度单")
    @CacheEvict(key = "#dispatch.id")
    public AjaxResult editDispatch(@RequestBody Dispatch dispatch) {

        if (dispatch.getTaskId() != null) throw new ServiceException("该调度单已与任务单关联，不允许修改");
        if (!Objects.equals(dispatch.getStatus(), Dispatch.NOT_OUTPUT))
            return AjaxResult.error("商品已出库不可更改信息");
        Dispatch prevDispatch = dispatchService.getById(dispatch.getId());

        if (prevDispatch == null) return AjaxResult.error("调度单不存在");
        if (prevDispatch.getTaskId() != null) throw new ServiceException("该调度单已与任务单关联，不允许修改");


        AjaxResult remoteSubwareResult = substationClient.getSubwareId(dispatch.getSubstationId());
        if (remoteSubwareResult.isError()) throw new ServiceException("获取分库ID失败");
        Long subwareId = (Long) remoteSubwareResult.get("data");

        //若是需要修改调度的数量，需要将已分配减去原来的，可分配添加上原来的，已分配添加新增的，可分配减去新增的
        AjaxResult reDispatchSuccess = centerWareClient.reDispatch(prevDispatch.getProductId(), prevDispatch.getProductNum(), dispatch.getProductNum());
        if (reDispatchSuccess.isError()) throw new ServiceException("修改库存失败: " + reDispatchSuccess.getMsg());

        //更新调度单
        if (!dispatchService.updateById(dispatch)) throw new ServiceException("更新调度单失败");
        CenterOutputVo centerOutputVo = new CenterOutputVo(dispatch.getId(), null, dispatch.getProductId(), dispatch.getProductName(), null,
                dispatch.getProductNum(), InputOutputType.DISPATCH_OUT, dispatch.getPlanTime(), null, dispatch.getSubstationId(), subwareId);

        AjaxResult updateResult = centerWareClient.update(centerOutputVo);
        if (updateResult.isError()) throw new ServiceException("修改仓库出库调度记录失败:" + updateResult.getMsg());
        return AjaxResult.success("修改成功");
    }


    @GetMapping("/list")
    @ApiOperation(value = "获取调度单列表", notes = "获取调度单列表")
    public AjaxResult getDispatchList() {
        List<Dispatch> dispatches = dispatchService.list();
        if (dispatches == null || dispatches.size() == 0) return AjaxResult.error("无调度单存在");
        return AjaxResult.success(dispatches);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取调度单信息", notes = "获取调度单信息")
    @Cacheable(key = "#id")
    public AjaxResult getDispatchInfo(@ApiParam("调度单ID") @PathVariable("id") Long id) {
        Dispatch dispatch = dispatchService.getById(id);
        if (dispatch == null) return AjaxResult.error("调度单不存在");
        //需要查询商品 填充vo
        DispatchVo dispatchVo = new DispatchVo();
        BeanUtils.copyProperties(dispatch, dispatchVo);
        Long productId = dispatch.getProductId();
        //商品ID 查询库存信息
        StorageVo storage = centerWareClient.getStorage(productId);
        if (storage == null) return AjaxResult.error("库存信息不存在");
        BeanUtils.copyProperties(storage, dispatchVo);
        return AjaxResult.success(dispatchVo);
    }


}

