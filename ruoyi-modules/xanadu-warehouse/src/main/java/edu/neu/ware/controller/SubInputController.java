package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.ware.entity.CenterOutput;
import edu.neu.ware.entity.SubInput;
import edu.neu.ware.entity.SubOutput;
import edu.neu.ware.entity.SubStorageRecord;
import edu.neu.ware.feign.TaskClient;
import edu.neu.ware.service.CenterOutputService;
import edu.neu.ware.service.CenterwareService;
import edu.neu.ware.service.SubInputService;
import edu.neu.ware.service.SubStorageRecordService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 */
@RestController
@RequestMapping("/ware/subInput")
@Transactional
public class SubInputController {

    /**
     * 调拨入库是指库房在接到中心库房调拨过来的商品后进行入库处理。
     * 输入正确的日期段、选择出入库类型为“调拨入库 ”，进 行查询， 显示需要进行调拨入库的列表，包含以下内容：
     * 选择项、调拨单号、 商品代码、商品名称、入库数量、 任务号、 日期功能点：
     * 入库：订单状态改为“配送站到货 ”，将 任务单状态改为“ 可分配 ”。
     */

    @Autowired
    CenterwareService centerwareService;

    @Autowired
    SubInputService subInputService;

    @Autowired
    SubStorageRecordService subStorageRecordService;


    @Autowired
    CenterOutputService centerOutputService;


    @Autowired
    TaskClient taskClient;


    @GetMapping("/listDispatch/{subwareId}")
    @ApiOperation("查询子库的所有调拨入库的单子")
    public AjaxResult listDispatch(@PathVariable("subwareId") Long subwareId) {
        QueryWrapper<CenterOutput> queryWrapper = new QueryWrapper<CenterOutput>().eq("subware_id", subwareId).eq("status", InputOutputStatus.OUTPUT)
                .eq("output_type", InputOutputType.DISPATCH_OUT);
        //拿到了列表后，需要回显
        return AjaxResult.success(centerOutputService.list(queryWrapper));
    }


    @GetMapping("/listRefund/{subwareId}")
    @ApiOperation("查询子库的所有重新入库的单子")
    public AjaxResult listRefund(@PathVariable("subwareId") Long subwareId) {
        QueryWrapper<SubInput> queryWrapper = new QueryWrapper<SubInput>().eq("subware_id", subwareId).eq("input_type", InputOutputType.RESTORE);
        //拿到了列表后，需要回显
        return AjaxResult.success(subInputService.list(queryWrapper));
    }

    @PostMapping("/confirmDispatch/{id}")
    @ApiOperation("确认中心仓库调度商品入库")
    public AjaxResult confirmDispatch(@PathVariable("id") Long id) {
        //拿到出库单信息
        CenterOutput centerOutput = centerOutputService.getById(id);
        //填充入库单信息
        SubInput subInput = new SubInput(null, centerOutput.getId(), InputOutputType.DISPATCH, centerOutput.getProductId(),
                centerOutput.getProductName(), centerOutput.getOutputNum(), new Date(), centerOutput.getSubwareId(), centerOutput.getProductPrice(), centerOutput.getSupplierId(), centerOutput.getTaskId());
        //插入入库单
        boolean saved = subInputService.save(subInput);
        if (!saved) throw new ServiceException("插入入库单失败");

        //更新出库单状态
        centerOutput.setStatus(InputOutputStatus.SUB_INPUT);
        boolean b = centerOutputService.updateById(centerOutput);
        if (!b) throw new ServiceException("更新出库单状态失败");

        //检查这一个任务是不是都入库了，如果都入库了就更新任务单状态和订单状态
        Integer outCount = centerOutputService.checkTaskStatus(centerOutput.getTaskId(), InputOutputStatus.OUTPUT);
        Integer notOutCount = centerOutputService.checkTaskStatus(centerOutput.getTaskId(), InputOutputStatus.NOT_OUTPUT);
        if (outCount + notOutCount == 0) {
            //更新任务单状态，并且同时更新订单的状态
            AjaxResult ajaxResult = taskClient.updateTaskStatus(centerOutput.getTaskId(), TaskStatus.ASSIGNABLE);
            if (ajaxResult.isError())
                throw new ServiceException(ajaxResult.getMsg());
        }
        //更新商品库存，需要注意子站可能没有该商品需要插入
        //检查是否存在该商品
        QueryWrapper<SubStorageRecord> eq = new QueryWrapper<SubStorageRecord>().eq("product_id", centerOutput.getProductId()).eq("subware_id", centerOutput.getSubwareId());
        SubStorageRecord record = subStorageRecordService.getOne(eq);

        Long recordId = null;
        SubStorageRecord subStorageRecord;

        //如果无法查询到该商品记录，说明该商品没有入库过，需要插入
        if (record == null) {
            subStorageRecord = new SubStorageRecord(null, centerOutput.getSubwareId(), centerOutput.getProductId(), 0, centerOutput.getProductName(),
                    centerOutput.getProductPrice(), null, null, 0, 0, 0);
            //插入数据库
            boolean save = subStorageRecordService.save(subStorageRecord);
            if (!save) throw new ServiceException("插入商品库存失败");
            recordId = subStorageRecord.getId();
            subStorageRecord = subStorageRecordService.getById(recordId);
        } else
            subStorageRecord = record;


        //更新商品库存，增加可分配数量
        subStorageRecord.setTotalNum(subStorageRecord.getTotalNum() + centerOutput.getOutputNum());
        subStorageRecord.setAllocateAbleNum(subStorageRecord.getAllocateAbleNum() + centerOutput.getOutputNum());
        boolean updateSuccess = subStorageRecordService.updateById(subStorageRecord);
        if (!updateSuccess) throw new ServiceException("更新商品库存失败");
        return AjaxResult.success("商品调拨入库成功");
    }


}