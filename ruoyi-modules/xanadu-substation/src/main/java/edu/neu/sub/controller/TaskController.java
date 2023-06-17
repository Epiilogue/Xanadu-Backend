package edu.neu.sub.controller;


import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.Task;
import edu.neu.sub.feign.OrderClient;
import edu.neu.sub.feign.SubwareClient;
import edu.neu.sub.feign.TaskClient;
import edu.neu.sub.service.SubstationService;
import edu.neu.sub.service.TaskService;
import edu.neu.sub.vo.ProductVo;
import edu.neu.sub.vo.TaskVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务单 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 04:10:48
 */
@RestController
@RequestMapping("/sub/task")
public class TaskController {

    /**
     * 获取子站对应的所有任务
     */
    @Autowired
    TaskService taskService;

    @Autowired
    TaskClient taskClient;
    @Autowired
    SubstationService substationService;

    @Autowired
    SubwareClient subwareClient;


    @GetMapping("/list/{subId}")
    @ApiOperation(value = "获取子站所有任务记录,对应所有任务页面")
    public AjaxResult list(@PathVariable("subId") Long subId) {
        AjaxResult taskBySubstationId = taskClient.getTaskBySubstationId(subId);
        if (taskBySubstationId == null || taskBySubstationId.isError()) {
            return AjaxResult.error("查询分站任务列表失败");
        }
        //转化
        String data = JSON.toJSONString(taskBySubstationId.get("data"));
        List<TaskVo> taskVos = JSON.parseArray(data, TaskVo.class);
        if (taskVos == null || taskVos.size() == 0) {
            return AjaxResult.error("该分站没有任务");
        }
        return AjaxResult.success(taskVos);
    }

    @GetMapping("/listCouriers/{subId}")
    @ApiOperation(value = "获取子站对应的所有快递员")
    public AjaxResult listCouriers(@PathVariable("subId") Long subId) {
        //获取所有的快递员ID
        List<Long> courierIds = substationService.getCourierBySubstationId(subId);
        if (courierIds == null) {
            return AjaxResult.error("查询分站快递员列表失败");
        }
        if (courierIds.size() == 0) return AjaxResult.error("该分站未指定快递员");
        return AjaxResult.success(courierIds);
    }

    @GetMapping("/listHanding/{subId}")
    @ApiOperation(value = "获取子站所有正在处理的任务记录,对应正在处理的任务页面")
    public AjaxResult listHanding(@PathVariable("subId") Long subId) {
        //此时返回的是本站所有已分配的记录等，支持删除记录
        return AjaxResult.success(taskService.list());
    }

    @DeleteMapping("/delete/{taskId}")
    @ApiOperation(value = "删除任务记录，只是删除本站记录视图，不是真正删除任务记录")
    public AjaxResult delete(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getById(taskId);
        String status = task.getTaskStatus();
        //只能删除失败 完成 部分完成任务记录
        if (!status.equals(TaskStatus.COMPLETED) && !status.equals(TaskStatus.FAILED) && !status.equals(TaskStatus.PARTIAL_COMPLETED)) {
            return AjaxResult.error("任务进行中不允许删除");
        }
        boolean b = taskService.removeById(taskId);
        if (b) return AjaxResult.success("删除成功");
        else return AjaxResult.error("删除失败");
    }


    @PostMapping("/assign/{subId}/{courierId}")
    @ApiOperation(value = "分配任务给快递员,根据任务类型分配任务")
    public AjaxResult assign(@PathVariable("subId") Long subId,
                             @PathVariable("courierId") Long courierId,
                             @RequestBody TaskVo taskVo) {
        Task task = new Task();
        //拿到所有的商品列表以及商品数量等信息，去对应的分站仓库查询，是不是有该商品或者有足够的库存
        //有的话就设置为已分配否则提示失败，需要调度中心调拨商品
        switch (taskVo.getTaskType()) {
            //如果是付款单以及退货单不需要提货出货，直接分配任务
            case TaskType.PAYMENT:
            case TaskType.RETURN:
                //更新订单状态和任务状态为已分配
                AjaxResult ajaxResult = taskClient.updateTaskStatus(taskVo.getId(), TaskStatus.ASSIGNED);
                if (ajaxResult == null) throw new RuntimeException("更新任务状态失败");
                if (ajaxResult.isError()) throw new RuntimeException(ajaxResult.getMsg());
                break;
            case TaskType.DELIVERY:
            case TaskType.EXCHANGE:
            case TaskType.PAYMENT_DELIVERY:
                //需要检查分库的商品库存是否足够进行分配
                List<ProductVo> products = taskVo.getProducts();
                //获取对应的分库ID
                Long subwareId = substationService.getById(subId).getSubwareId();
                //映射成map，key为商品ID，value为商品数量
                HashMap<Long, Integer> longIntegerHashMap = new HashMap<>();
                for (ProductVo product : products) longIntegerHashMap.put(product.getId(), product.getNumber());
                AjaxResult check = subwareClient.check(subwareId, longIntegerHashMap);
                if (check == null) throw new RuntimeException("检查库存失败");
                if (check.isError()) throw new RuntimeException(check.getMsg());
                AjaxResult lockSuccess = subwareClient.lock(subwareId, longIntegerHashMap);
                if (lockSuccess == null || lockSuccess.isError()) throw new RuntimeException("分配库存失败");
                //库存足够，更新任务状态以及锁定库存
                AjaxResult updateTaskStatus = taskClient.updateTaskStatus(taskVo.getId(), TaskStatus.ASSIGNED);
                if (updateTaskStatus == null) throw new RuntimeException("更新任务状态失败");
                if (updateTaskStatus.isError()) throw new RuntimeException(updateTaskStatus.getMsg());
        }

        taskVo.setTaskStatus(TaskStatus.ASSIGNED);
        BeanUtils.copyProperties(taskVo, task);
        task.setCourierId(courierId);
        task.setProductsJson(JSON.toJSONString(taskVo.getProducts()));
        //插入数据库
        boolean saved = taskService.save(task);
        if (!saved) throw new RuntimeException("分配任务失败");

        return AjaxResult.success("分配任务成功");
    }


    @GetMapping("/getTasksByCourierId/{subId}/{courierId}")
    @ApiOperation(value = "根据快递员ID获取所有任务记录,列表后有两个按钮，一个是填写回执，一个是查看详情，查看详情可以任务里面的商品列表")
    public AjaxResult getTasksByCourierId(@PathVariable("courierId") Long courierId) {
        List<Task> tasks = taskService.getTasksByCourierId(courierId);
        if (tasks == null) return AjaxResult.error("查询失败");
        tasks.forEach(task -> task.setProducts(JSON.parseArray(task.getProductsJson(), ProductVo.class)));
        return AjaxResult.success(tasks);
    }

    //对于不同的任务类型，快递员可以执行不同的操作
    @GetMapping("/takeProducts/{taskId}")
    @ApiOperation(value = "快递员取货，根据任务类型执行不同的操作")
    public AjaxResult takeProducts(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null) return AjaxResult.error("任务不存在");
        String taskType = task.getTaskType();
        //根据任务类型执行不同的操作
        switch (taskType) {
            case TaskType.PAYMENT:
            case TaskType.RETURN:
                //不需要取货
                return AjaxResult.error("该任务类型不需要取货");
            case TaskType.DELIVERY:
            case TaskType.EXCHANGE:
            case TaskType.PAYMENT_DELIVERY:
                //减去库存，更新任务状态以及订单状态为已取货，需要更新本地任务以及远程任务
                //1.拿到任务对应的商品列表
                String productsJson = task.getProductsJson();
                List<ProductVo> productVos = JSON.parseArray(productsJson, ProductVo.class);
                //2.拿到对应的分库id
                Long subwareId = substationService.getById(task.getSubId()).getSubwareId();
                //3.映射成map，key为商品ID，value为商品数量,
                HashMap<Long, Integer> longIntegerHashMap = new HashMap<>();
                for (ProductVo productVo : productVos) longIntegerHashMap.put(productVo.getId(), productVo.getNumber());
                //4.减去库存,生成出库记录
                AjaxResult reduceResult = subwareClient.reduce(subwareId, task.getId(), longIntegerHashMap);
                if (reduceResult == null) throw new RuntimeException("出库失败");
                if (reduceResult.isError()) throw new RuntimeException(reduceResult.getMsg());
                //5.更新任务状态以及订单状态为已取货,需要更新本地任务以及远程任务
                //6.更新本地任务状态
                task.setTaskStatus(TaskStatus.RECEIVED);
                boolean b = taskService.updateById(task);
                if (!b) throw new RuntimeException("更新任务状态失败");
                //7.更新远程任务状态
                AjaxResult updateTaskStatus = taskClient.updateTaskStatus(task.getId(), TaskStatus.RECEIVED);
                if (updateTaskStatus == null) throw new RuntimeException("更新远程任务状态失败");
                if (updateTaskStatus.isError()) throw new RuntimeException(updateTaskStatus.getMsg());
        }
        return AjaxResult.success("取货成功");
    }

    //接下来需要根据不同的类型填写不同的回执单，并产生不同的结果
    /**
     * 填写回执单
     */
    @PostMapping("/fillPaymentReceipt/{taskId}/{userID}")
    @ApiOperation(value = "填写收款回执单")
    public AjaxResult fillPaymentReceipt() {

        return null;

    }



}

