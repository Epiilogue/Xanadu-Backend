package edu.neu.sub.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.ReceiptStatus;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.Receipt;
import edu.neu.sub.entity.ReceiptProduct;
import edu.neu.sub.entity.Task;
import edu.neu.sub.feign.SubwareClient;
import edu.neu.sub.feign.TaskClient;
import edu.neu.sub.mapper.ReceiptProductMapper;
import edu.neu.sub.service.*;
import edu.neu.sub.vo.PaymentReceiptVo;
import edu.neu.sub.vo.ProductVo;
import edu.neu.sub.vo.ReceiptVo;
import edu.neu.sub.vo.TaskVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    ReceiptService receiptService;

    @Autowired
    ReceiptProductService receiptProductService;

    @Autowired
    PendingProductService pendingProductService;


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
                for (ProductVo product : products) longIntegerHashMap.put(product.getProductId(), product.getNumber());
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

    //对于不同的任务类型，需要执行不同的操作，比如付款单，需要付款，退货单不需要取货可以直接录入回执，送货换货就需要取货
    @GetMapping("/takeProducts/{taskId}")
    @ApiOperation(value = "取货，根据任务类型执行不同的操作，前端需要")
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
                for (ProductVo productVo : productVos)
                    longIntegerHashMap.put(productVo.getProductId(), productVo.getNumber());
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
     * 回执录入
     */
    @PostMapping("/fillPaymentReceipt")
    @ApiOperation(value = "填写收款回执单，前端若为收款任务则需要调用该接口，注意收款只有成功与失败")
    public AjaxResult fillPaymentReceipt(@RequestBody PaymentReceiptVo paymentReceiptVo) {
        //根据ID拿到任务信息
        Long taskId = paymentReceiptVo.getTaskId();
        if (taskId == null) return AjaxResult.error("任务ID不能为空");
        //检查回执状态是否合法
        Task task = taskService.getById(taskId);
        if (task == null) return AjaxResult.error("回执任务不存在");
        //检查任务类型是不是首款类型并且任务状态是不是已分配
        if (!TaskType.PAYMENT.equals(task.getTaskType()) || !TaskStatus.ASSIGNED.equals(task.getTaskStatus()))
            return AjaxResult.error("当前任务状态无法填写回执单");
        Receipt receipt = new Receipt();
        BeanUtils.copyProperties(paymentReceiptVo, receipt);
        BeanUtils.copyProperties(task, receipt);
        receipt.setId(null);
        receipt.setTaskId(taskId);
        receipt.setCreateTime(new Date());
        receipt.setPlanReceipt(task.getTotalAmount());
        //检查回执任务状态，只允许是已完成或者失败
        if (!ReceiptStatus.COMPLETED.equals(receipt.getState()) && !ReceiptStatus.FAILED.equals(receipt.getState()))
            return AjaxResult.error("回执状态不合法");
        if (receipt.getState().equals(ReceiptStatus.COMPLETED)) receipt.setInputMoney(receipt.getPlanReceipt());
        else receipt.setInputMoney(0.0);
        //检查客户满意度是否在0-10的合法范围内
        if (receipt.getFeedback() < 0 || receipt.getFeedback() > 10)
            return AjaxResult.error("客户满意度不合法");
        //签收时间必须要在当前时间之前
        if (receipt.getSignTime().after(new Date())) return AjaxResult.error("签收时间不合法");
        //检查同类型同任务号的回执单是否存在
        QueryWrapper<Receipt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId).eq("task_type", TaskType.PAYMENT);
        Receipt existReceipt = receiptService.getOne(queryWrapper);
        if (existReceipt != null) return AjaxResult.error("回执单已存在");
        //根据收款回执单的最终结果执行不同的操作，如果是失败的单子，就将订单状态以及所有的本地或者远程任务状态更新为失败
        boolean success;
        switch (receipt.getState()) {
            case ReceiptStatus.FAILED:
                task.setTaskStatus(TaskStatus.FAILED);
                //更新本地任务状态
                success = taskService.updateById(task);
                if (!success) throw new RuntimeException("更新本地任务状态失败");
                //更新远程任务状态
                AjaxResult ajaxResult = taskClient.updateTaskStatus(task.getId(), TaskStatus.FAILED);
                if (ajaxResult == null) throw new RuntimeException("更新远程任务状态失败");
                if (ajaxResult.isError()) throw new RuntimeException(ajaxResult.getMsg());
                break;
            case ReceiptStatus.COMPLETED:
                //1.更新本地任务状态
                task.setTaskStatus(TaskStatus.COMPLETED);
                success = taskService.updateById(task);
                if (!success) throw new RuntimeException("更新本地任务状态失败");
                //2.更新远程任务状态
                AjaxResult ajaxResult1 = taskClient.updateTaskStatus(task.getId(), TaskStatus.COMPLETED);
                if (ajaxResult1 == null) throw new RuntimeException("更新远程任务状态失败");
                //3.创建新的任务，任务为送货任务，需要进行远程调用传入订单ID
                AjaxResult createResult = taskClient.createDeliveryTask(task.getOrderId(), task.getSubId(), substationService.getById(task.getSubId()).getSubwareId());
                if (createResult == null) throw new RuntimeException("创建送货任务失败");
                if (createResult.isError()) throw new RuntimeException(createResult.getMsg());
                break;
        }
        return AjaxResult.success("收款回执单填写成功");
    }


    /**
     * 填写其他回执单，对于其他回执单，都需要携带有实际退货或者签收的商品数量
     * 对于货到付款来说，对于未完成的商品，我们会要求输入退货数量，但是此时无
     * 法输入不可退货的商品，以及超过可退货数量的商品对于退货以及送货来说，
     * 实际金额为退款金额，对于换货来说不收钱，对于送货来说实际金额为收款金额
     **/
    @PostMapping("/fillReceipt")
    @ApiOperation(value = "填写回执单，可能是退货，换货，送货的回执单填写，如果选择部分完成，需要填写每一个商品数量，前端记得回传之前拿到的商品信息")
    public AjaxResult fillReceipt(@RequestBody ReceiptVo receiptVo) {
        //根据ID拿到任务信息
        Long taskId = receiptVo.getTaskId();
        if (taskId == null) return AjaxResult.error("任务ID不能为空");
        //检查回执状态是否合法
        Task task = taskService.getById(taskId);
        if (task == null) return AjaxResult.error("回执任务不存在");
        Long subwareId = substationService.getById(task.getSubId()).getSubwareId();

        switch (task.getTaskType()) {
            case TaskType.DELIVERY:
            case TaskType.PAYMENT_DELIVERY:
            case TaskType.EXCHANGE:
                //检查任务状态是不是已领货
                if (!TaskStatus.RECEIVED.equals(task.getTaskStatus()))
                    return AjaxResult.error("当前任务状态无法填写回执单");
                break;
            case TaskType.RETURN:
                //检查任务状态是不是已分配
                if (!TaskStatus.ASSIGNED.equals(task.getTaskStatus()))
                    return AjaxResult.error("当前任务状态无法填写回执单");
                break;
            default:
                return AjaxResult.error("当前任务类型无法填写回执单");
        }
        Receipt receipt = new Receipt();
        BeanUtils.copyProperties(receiptVo, receipt);
        BeanUtils.copyProperties(task, receipt);
        receipt.setId(null);
        receipt.setTaskId(taskId);
        receipt.setCreateTime(new Date());
        receipt.setPlanReceipt(task.getTotalAmount());
        receipt.setPlanNum(task.getNumbers());
        //检查回执任务状态，只允许是已完成或者失败或者部分完成
        if (!ReceiptStatus.COMPLETED.equals(receipt.getState()) &&
                !ReceiptStatus.FAILED.equals(receipt.getState()) &&
                !ReceiptStatus.PARTIAL_COMPLETED.equals(receipt.getState()))
            return AjaxResult.error("回执状态不合法");

        //检查回执单携带的商品信息是否合法
        List<ProductVo> products = receiptVo.getProducts();
        if (products == null) return AjaxResult.error("无商品信息反馈");
        for (ProductVo product : products)
            if ((!product.getRefundAble()) && product.getActualNumber() < product.getNumber())
                return AjaxResult.error("存在不可退货商品");
        //检查客户满意度是否在0-10的合法范围内
        if (receipt.getFeedback() < 0 || receipt.getFeedback() > 10)
            return AjaxResult.error("客户满意度不合法");
        //签收时间必须要在当前时间之前
        if (receipt.getSignTime().after(new Date())) return AjaxResult.error("签收时间不合法");

        //检查同类型同任务号的回执单是否存在
        QueryWrapper<Receipt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId).eq("task_type", task.getTaskType());
        Receipt existReceipt = receiptService.getOne(queryWrapper);
        if (existReceipt != null) return AjaxResult.error("回执单已存在");
        boolean save = receiptService.save(receipt);
        if (!save) return AjaxResult.error("保存回执单失败");
        Long receiptId = receipt.getId();
        //2.更新本地状态
        task.setTaskStatus(TaskStatus.COMPLETED);
        task.setReceiptId(receiptId);
        //3.保存商品信息，对于不同的类型我们应当保存不同的商品信息
        List<ReceiptProduct> receiptProducts = receiptProductService.convertAndSave(receiptId, products, receipt.getState(), task.getTaskType());
        if (receiptProducts == null) throw new ServiceException("保存商品信息失败");
        receipt.setSignNum(receiptProducts.stream().mapToInt(ReceiptProduct::getSignNum).sum());
        receipt.setRefundNum(receiptProducts.stream().mapToInt(ReceiptProduct::getReturnNum).sum());
        receipt.setInputMoney(receiptProducts.stream().mapToDouble(ReceiptProduct::getInputMoney).sum());
        receipt.setOutputMoney(receiptProducts.stream().mapToDouble(ReceiptProduct::getOutputMoney).sum());
        boolean b = receiptService.updateById(receipt);
        if (!b) throw new ServiceException("更新回执单失败");
        //4.自动生成待处理商品信息单，保存到数据库，等待操作人员入库或者退货处理
        boolean isSuccess = pendingProductService.convertAndSave(taskId, products, receipt.getState(), task.getTaskType(), subwareId);
        if (!isSuccess) throw new ServiceException("保存待处理商品信息失败");
        //5.更新远程状态
        AjaxResult ajaxResult = taskClient.updateTaskStatus(task.getId(), receipt.getState());
        if (ajaxResult == null || ajaxResult.isError()) throw new ServiceException("更新远程任务状态失败");
        return AjaxResult.success("回执单填写成功");
    }


}

