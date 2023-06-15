package edu.neu.ware.controller;


import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.ware.entity.CenterOutput;
import edu.neu.ware.entity.CenterStorageRecord;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.feign.CCOrderClient;
import edu.neu.ware.feign.SupplierClient;
import edu.neu.ware.feign.TaskClient;
import edu.neu.ware.service.CenterOutputService;
import edu.neu.ware.service.CenterStorageRecordService;
import edu.neu.ware.service.SubwareService;
import edu.neu.ware.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 */
@RestController
@RequestMapping("/ware/centerOutput")
@Transactional
public class CenterOutputController {
    @Autowired
    CenterOutputService centerOutputService;

    @Autowired
    SubwareService subwareService;

    @Autowired
    CenterStorageRecordService centerStorageRecordService;

    @Autowired
    TaskClient taskClient;

    @Autowired
    SupplierClient supplierClient;

    @Autowired
    CCOrderClient ccOrderClient;

    /**
     * 中心仓库可以查两个输出，一个是退货出库，一个是调度出库,两个独立拥有不同的字段
     */

    @GetMapping("/getDispatchOutput")
    @ApiOperation("获取所有的调度出库记录")
    public AjaxResult getDispatchOutput() {
        List<CenterOutput> dispatchOutputList = centerOutputService.list(new QueryWrapper<CenterOutput>().eq("output_type", InputOutputType.DISPATCH_OUT));
        //获取到了所有的调度出库的列表
        //映射为CenterDispatchOutputVo
        List<CenterDispatchOutputVo> list = dispatchOutputList.stream().map(
                dispatchOutput -> {
                    CenterDispatchOutputVo centerDispatchOutputVo = new CenterDispatchOutputVo();
                    BeanUtils.copyProperties(dispatchOutput, centerDispatchOutputVo);
                    return centerDispatchOutputVo;
                }
        ).collect(Collectors.toList());
        return AjaxResult.success(list);
    }


    /*选择项、商品分类、商品代码、商品名称、供应商、计量单位、退货数量、 日期 */
    @GetMapping("/getReturnOutput")
    @ApiOperation("获取所有的退货出库记录")
    public AjaxResult getReturnOutput() {
        List<CenterOutput> returnOutputList = centerOutputService.list(new QueryWrapper<CenterOutput>().eq("output_type", InputOutputType.RETURN_OUT));
        //获取到了所有的中心退货出库的列表
        //映射为CenterDispatchOutputVo
        //首先需要远程调用获取对应的供应商信息
        Map<Long, String> supplierNames = supplierClient.getSupplierNames();

        List<CenterRefundOutputVo> list = returnOutputList.stream().map(
                returnOutput -> {
                    CenterRefundOutputVo centerRefundOutputVo = new CenterRefundOutputVo();
                    BeanUtils.copyProperties(returnOutput, centerRefundOutputVo);
                    //设置供应商名字
                    Long supplierId = returnOutput.getSupplierId();
                    String supplierName = supplierNames.get(supplierId);
                    centerRefundOutputVo.setSupplierName(supplierName);
                    return centerRefundOutputVo;
                }
        ).collect(Collectors.toList());
        return AjaxResult.success(list);
    }


    @PutMapping("/confirm/{id}/{number}")
    @ApiOperation("确认出库，传入记录ID以及确认的出库数量")
    public AjaxResult confirm(@PathVariable("id") Long id, @PathVariable("number") Integer number) {
        if (id == null || number == null) return AjaxResult.error("确认失败，参数不能为空");
        if (number <= 0) return AjaxResult.error("确认失败，出库数量必须大于0");
        //根据ID查询出库记录
        CenterOutput centerOutput = centerOutputService.getById(id);
        if (centerOutput == null) return AjaxResult.error("确认失败，未找到该出库记录");
        //检查状态是否为未出库
        String status = centerOutput.getStatus();
        if (!status.equals(InputOutputStatus.NOT_OUTPUT)) return AjaxResult.error("确认失败，该出库记录已经出库");
        //拿到出库记录的出库数量
        Integer outputNum = centerOutput.getOutputNum();
        //获取出库类型
        String outputType = centerOutput.getOutputType();
        boolean updateSuccess;
        switch (outputType) {
            case InputOutputType.RETURN_OUT:
                //TODO: 退货出库，选择可分配数量，退货出库
                updateSuccess = centerStorageRecordService.update(new UpdateWrapper<CenterStorageRecord>().setSql("allocate_able_num=allocate_able_num-" + number).
                        eq("product_id", centerOutput.getProductId()).ge("allocate_able_num", number));
                if (!updateSuccess) throw new ServiceException("出库失败，实际出库数量大于可分配数量");
                break;
            case InputOutputType.DISPATCH_OUT:
                //如果是调拨出库，需要把原来的分配数量划到可分配数量，然后减去实际出库的可分配数量，如果小于0，返回错误并提示失败
                updateSuccess = centerStorageRecordService.update(new UpdateWrapper<CenterStorageRecord>().setSql("allocate_able_num=allocate_able_num+" + outputNum).
                        setSql("allocate_num=allocate_num-" + outputNum).eq("product_id", centerOutput.getProductId()).ge("allocate_able_num", 0));
                //实际出库，出库失败则回滚
                if (!updateSuccess) throw new ServiceException("出库失败，已分配数量不足");
                updateSuccess = centerStorageRecordService.update(new UpdateWrapper<CenterStorageRecord>().setSql("allocate_able_num=allocate_able_num-" + number).
                        eq("product_id", centerOutput.getProductId()).ge("allocate_able_num", number));
                if (!updateSuccess) throw new ServiceException("出库失败，可分配数量不足");
                //将该记录状态更新为已出库
                //如果记录存在有taskId，我们需要检查该taskID是否存在有未完成的任务，如果没有，我们需要把状态更新为中心仓库出库
                if (centerOutput.getTaskId() != null) {
                    //拿到任务单，检查是否存在未完成的任务
                    QueryWrapper<CenterOutput> queryWrapper = new QueryWrapper<CenterOutput>().eq("task_id", centerOutput.getTaskId()).eq("status", InputOutputStatus.NOT_OUTPUT);
                    long count = centerOutputService.count(queryWrapper);
                    if (count == 0) {
                        //更新状态为中心仓库出库,根据任务单找到订单号，然后根据订单号更新状态
                        Long orderIdByTaskId = taskClient.getOrderIdByTaskId(centerOutput.getTaskId());
                        if (orderIdByTaskId == null) throw new ServiceException("出库失败，未找到该任务单对应的订单");
                        //根据订单ID更新订单状态为中心仓库出库
                        Boolean success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.CENTER_OUTBOUND, Collections.singletonList(orderIdByTaskId));
                        if (!success) throw new ServiceException("出库失败，更新订单状态失败");
                    }
                }
                break;
        }
        //需要更新total_num
        updateSuccess = centerStorageRecordService.update(new UpdateWrapper<CenterStorageRecord>().setSql("total_num=total_num-" + number).
                eq("product_id", centerOutput.getProductId()).ge("total_num", number));
        if (!updateSuccess) throw new ServiceException("出库失败，更新总库存量失败");
        centerOutput.setStatus(InputOutputStatus.OUTPUT);
        centerOutput.setOutputTime(new Date());
        centerOutput.setOutputNum(number);
        //TODO：动态获取登录用户作为操作人
        centerOutput.setOperatorId(1L);
        return AjaxResult.success(centerOutputService.updateById(centerOutput));
    }


    @GetMapping("/printInventoryList")
    @ApiOperation("打印出库单接口,只找未出库的")
    public AjaxResult printInventoryList(@RequestParam(value = "date") Date date,
                                         @RequestParam(value = "productName", required = false) String productName) {
        //获取指定日期的出库记录,需要获取当日所有时间的出库记录,从当日00：00 到当日 23：59
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date start = calendar.getTime();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date end = calendar.getTime();
        //获取所有的出库记录，字段为require_time
        List<CenterOutput> centerOutputs = centerOutputService.list(new QueryWrapper<CenterOutput>().between("require_time", start, end).eq("status", InputOutputStatus.NOT_OUTPUT));
        if (productName != null && !productName.equals("")) {
            centerOutputs = centerOutputs.stream().filter(centerOutput -> centerOutput.getProductName().contains(productName)).collect(Collectors.toList());
        }
        //根据商品进行分类统计

        Map<Long, String> supplierNames = supplierClient.getSupplierNames();
        HashMap<Long, InventoryVo> longInventoryHashMap = new HashMap<>();

        centerOutputs.forEach(centerOutput -> {
            //获取商品ID
            InventoryVo inventoryVo = longInventoryHashMap.getOrDefault(centerOutput.getProductId(), new InventoryVo(centerOutput));
            String supplierName;
            if (inventoryVo.getSupplierName() == null) {
                //说明初始化，此时需要查询总库存量以及供应商的名称
                supplierName = supplierNames.getOrDefault(centerOutput.getSupplierId(), "未知供应商");
                inventoryVo.setSupplierName(supplierName);
                Integer storage = centerStorageRecordService.getById(centerOutput.getProductId()).getTotalNum();
                inventoryVo.setTotalNum(storage);
                inventoryVo.setDate(date);
            }
            inventoryVo.setNumber(inventoryVo.getNumber() + centerOutput.getOutputNum());
            inventoryVo.setTotalPrice(inventoryVo.getTotalPrice() + centerOutput.getOutputNum() * centerOutput.getProductPrice());
        });


        return AjaxResult.success(longInventoryHashMap.values());
    }


    /**
     * 仓库名称、操作员、商品编号、商品名称、售价、数量、供销商、备注、商品数量总计、总金额、分发员、签收人、日期
     */
    @GetMapping("/printDistributionList")
    @ApiOperation("打印配送单接口,只找已出库的")
    public AjaxResult printDistributionList(@RequestParam("date") Date date
            , @RequestParam(value = "productName", required = false) String productName,
                                            @RequestParam("subwareId") Long SubwareId) {
        //获取指定日期的出库记录,需要获取当日所有时间的出库记录,从当日00：00 到当日 23：59
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date start = calendar.getTime();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date end = calendar.getTime();
        //查询子库名称
        String subwareName = subwareService.getById(SubwareId).getName();

        //获取所有的出库记录，字段为require_time
        List<CenterOutput> centerOutputs = centerOutputService.list(new QueryWrapper<CenterOutput>().between("", start, end).eq("status", InputOutputStatus.OUTPUT));
        centerOutputs = centerOutputs.stream().filter(centerOutput -> centerOutput.getSubwareId().equals(SubwareId)).collect(Collectors.toList());
        if (centerOutputs.size() == 0) return AjaxResult.error("未找到对应的出库记录");
        if (!StringUtil.isBlank(productName)) {
            centerOutputs = centerOutputs.stream().filter(centerOutput -> centerOutput.getProductName().contains(productName)).collect(Collectors.toList());
        }
        //根据商品进行分类统计
        HashMap<Long, InventoryVo> longInventoryHashMap = new HashMap<>();
        Map<Long, String> supplierNames = supplierClient.getSupplierNames();
        centerOutputs.forEach(centerOutput -> {
            //获取商品ID
            InventoryVo inventoryVo = longInventoryHashMap.getOrDefault(centerOutput.getProductId(), new InventoryVo(centerOutput));
            String supplierName;
            if (inventoryVo.getSupplierName() == null) {
                //说明初始化，此时需要查询总库存量以及供应商的名称
                supplierName = supplierNames.getOrDefault(centerOutput.getSupplierId(), "未知供应商");
                inventoryVo.setSupplierName(supplierName);
                Integer storage = centerStorageRecordService.getById(centerOutput.getProductId()).getTotalNum();
                inventoryVo.setTotalNum(storage);
                inventoryVo.setDate(date);
            }
            inventoryVo.setSubwareName(subwareName);
            inventoryVo.setNumber(inventoryVo.getNumber() + centerOutput.getOutputNum());
            inventoryVo.setOperatorId(centerOutput.getOperatorId());
            inventoryVo.setTotalPrice(inventoryVo.getTotalPrice() + centerOutput.getOutputNum() * centerOutput.getProductPrice());
        });
        return AjaxResult.success(longInventoryHashMap.values());
    }


    @PostMapping("/feign/add")
    @ApiOperation("添加出库记录")
    public Boolean add(@RequestBody CenterOutputVo centerOutputVo) {
        if (centerOutputVo == null) return false;
        CenterOutput centerOutput = new CenterOutput();
        BeanUtils.copyProperties(centerOutputVo, centerOutput);
        centerOutput.setStatus(InputOutputStatus.NOT_OUTPUT);
        return centerOutputService.save(centerOutput);
    }


    //修改
    @PostMapping("/feign/update")
    @ApiOperation("修改出库记录")
    public AjaxResult update(@RequestBody CenterOutputVo centerOutputVo) {
        if (centerOutputVo == null) return AjaxResult.error("修改失败,参数为空");
        //拿到ID
        Long outputId = centerOutputVo.getOutputId();
        //构建查询条件，output_id为outputId
        QueryWrapper<CenterOutput> centerOutputQueryWrapper = new QueryWrapper<CenterOutput>().eq("output_id", outputId);
        //根据查询条件查询出库记录
        CenterOutput centerOutput = centerOutputService.getOne(centerOutputQueryWrapper);
        if (centerOutput == null) return AjaxResult.error("修改失败,未找到该出库记录");
        centerOutputVo.setProductPrice(centerOutput.getProductPrice());//设置商品价格
        //检查一下子站是否存在
        if (centerOutputVo.getOutputType().equals(InputOutputType.DISPATCH_OUT)) {
            Subware subware = subwareService.getById(centerOutputVo.getSubwareId());
            if (subware == null) return AjaxResult.error("修改失败,未找到该子站");
        } else if (centerOutputVo.getOutputType().equals(InputOutputType.RETURN_OUT)) {
            //TODO: 找供应商是否存在

        }
        //修改信息
        BeanUtils.copyProperties(centerOutputVo, centerOutput);
        //更新
        boolean update = centerOutputService.update(centerOutput, centerOutputQueryWrapper);
        if (update) return AjaxResult.success("修改成功");
        return AjaxResult.error("修改失败");
    }

    //删除
    @DeleteMapping("/feign/delete")
    @ApiOperation("删除出库记录")
    public Boolean delete(@RequestParam("outputId") Long outputId) {
        if (outputId == null) return false;
        //构建查询条件，output_id为outputId
        QueryWrapper<CenterOutput> centerOutputQueryWrapper = new QueryWrapper<CenterOutput>().eq("output_id", outputId);
        //查找一下对应的记录
        CenterOutput centerOutput = centerOutputService.getOne(centerOutputQueryWrapper);
        if (centerOutput == null) return false;
        //如果是已出库的，不能删除
        if (centerOutput.getStatus().equals(InputOutputStatus.OUTPUT)) return false;
        //与订单关联的也不能删
        if (centerOutput.getTaskId() != null) return false;
        //删除
        return centerOutputService.remove(centerOutputQueryWrapper);
    }

}

