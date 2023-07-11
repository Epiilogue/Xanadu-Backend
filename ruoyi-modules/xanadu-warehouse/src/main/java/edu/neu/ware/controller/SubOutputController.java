package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.entity.SubOutput;
import edu.neu.ware.entity.SubStorageRecord;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.feign.SubstationClient;
import edu.neu.ware.service.*;
import edu.neu.ware.vo.CenterInputVo;
import edu.neu.ware.vo.PendingProductVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
 * <p>
 * 主要提供的是分库的出库记录查看
 * 具体逻辑为：分站填写回执，登记退货，分库可查看退货记录
 * 分库可以选择退货，然后填写商品数量，如果商品数量小于退货数量，则剩余商品重新入库，作为可分配商品信息
 */
@RestController
@RequestMapping("/ware/subOutput")
public class SubOutputController {

    @Autowired
    SubOutputService subOutputService;
    @Autowired
    SubStorageRecordService subStorageRecordService;
    @Autowired
    CenterInputService centerInputService;
    @Autowired
    SubstationClient substationClient;
    @Autowired
    SubwareService subwareService;

    @GetMapping("/list/{type}")
    @ApiOperation("分库出库记录查看,需要选择出库类型是 领货出库 或者是 退货出库,此处需要做成两个不同的字段表格，一个头是配送出库数量，一个是退货数量")
    public AjaxResult list(@PathVariable(value = "type", required = false) String type) {
        if (type == null)
            return AjaxResult.success(subOutputService.list());
        QueryWrapper<SubOutput> outputTypeQuery = new QueryWrapper<SubOutput>().eq("output_type", type);
        return AjaxResult.success(subOutputService.list(outputTypeQuery));
    }


    @PostMapping("/feign/refund")
    @ApiOperation("分库退货,由分站登记处选择退货出库，直接出库就行，不需要选择数量增加复杂度")
    public AjaxResult refund(@RequestBody PendingProductVo p) {
        SubOutput subOutput = new SubOutput(null, p.getTaskId(), p.getProductId(), p.getProductName(), p.getDealNumber(), InputOutputType.RETURN_OUT,
                new Date(), p.getSubwareId(), false, InputOutputStatus.NOT_OUTPUT, 0);
        QueryWrapper<SubStorageRecord> eq = new QueryWrapper<SubStorageRecord>().eq("product_id", p.getProductId()).eq("subware_id", p.getSubwareId());
        SubStorageRecord one = subStorageRecordService.getOne(eq);
        if (one == null) return AjaxResult.error("仓库不存在该商品记录");
        one.setRefundNum(one.getRefundNum() + p.getDealNumber());
        one.setTotalNum(one.getTotalNum() + p.getDealNumber());
        if (!subStorageRecordService.updateById(one)) throw new ServiceException("更新仓库商品记录失败");
        if (subOutputService.save(subOutput)) return AjaxResult.success("提交退货登记成功");
        throw new ServiceException("提交退货登记失败");
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation("删除退货记录,软删除")
    public AjaxResult delete(@PathVariable("id") Long id) {
        //先判断一下是否存在并且是未出库的记录
        SubOutput subOutput = subOutputService.getById(id);
        if (subOutput == null) return AjaxResult.error("不存在该记录");
        if (Objects.equals(subOutput.getStatus(), InputOutputStatus.NOT_OUTPUT))
            return AjaxResult.error("该记录未出库，无法删除");
        if (subOutputService.removeById(id)) return AjaxResult.success("删除成功");
        return AjaxResult.error("删除失败");
    }

    @PutMapping("/confirm/{id}/{number}")
    @ApiOperation("分库确认退货出库，需要填写退货数量")
    public AjaxResult confirm(@PathVariable("id") Long id, @PathVariable("number") Integer number) {
        //去掉之前的记录数量，避免数据的不一致性，不能去掉当前的数量
        //1.拿到记录，判断合法性
        SubOutput subOutput = subOutputService.getById(id);
        if (subOutput == null) return AjaxResult.error("不存在该记录");
        if (Objects.equals(subOutput.getStatus(), InputOutputStatus.OUTPUT)) return AjaxResult.error("该记录已经出库");
        //2.拿到原来的记录数量
        Integer prevNum = subOutput.getOutputNum();
        //3.更新出库记录状态
        subOutput.setStatus(InputOutputStatus.OUTPUT);
        subOutput.setActualNum(number);
        boolean b = subOutputService.updateById(subOutput);
        if (!b) throw new ServiceException("更新出库记录失败");
        //4.更新仓库存储记录，需要减掉数据
        QueryWrapper<SubStorageRecord> eq = new QueryWrapper<SubStorageRecord>().eq("product_id", subOutput.getProductId()).eq("subware_id", subOutput.getSubwareId());
        SubStorageRecord one = subStorageRecordService.getOne(eq);
        if (one == null) throw new ServiceException("仓库不存在该商品记录");
        //5.减去退货数量，减去登记数量
        one.setRefundNum(one.getRefundNum() - prevNum);
        one.setTotalNum(one.getTotalNum() - prevNum);
        boolean res = subStorageRecordService.updateById(one);
        if (!res) throw new ServiceException("更新仓库商品记录失败");

        //需要拿到分站ID，这个可以根据分库来查询
        AjaxResult substationIdRes = substationClient.getSubstationId(subOutput.getSubwareId());
        if (substationIdRes == null || substationIdRes.isError()) throw new ServiceException("查询分站信息失败");
        Long substationId = (Long) substationIdRes.get("data");
        //需要给中心仓库添加记录,传入的追踪记录ID是自己的id
        CenterInput centerInput = new CenterInput(null, subOutput.getId(), InputOutputType.RETURN, subOutput.getProductId(), subOutput.getProductName(), subOutput.getActualNum(), null,
                null, InputOutputStatus.NOT_INPUT, null, substationId, subOutput.getSubwareId(), 0);
        boolean save = centerInputService.save(centerInput);
        if (!save) throw new ServiceException("提交中心仓库退货记录失败");
        return AjaxResult.success("确认退货出库成功");
    }


    @ApiOperation("各分库出库量统计")
    @GetMapping("/analysis")
    public AjaxResult analysis() {
        //找到所有分库已经出库的记录，按照仓库id聚合，然后返回为一个map，key为仓库名，value为仓库出库量
        QueryWrapper<SubOutput> eq = new QueryWrapper<SubOutput>().eq("status", InputOutputStatus.OUTPUT);
        List<SubOutput> list = subOutputService.list(eq);
        Map<Long, List<SubOutput>> collect = list.stream().collect(Collectors.groupingBy(SubOutput::getSubwareId));
        HashMap<String, Integer> resultMap = new HashMap<>();
        collect.forEach((k, v) -> {
            Subware subware = subwareService.getById(k);
            String subwareName = subware.getName();
            //拿到仓库出库量
            Integer outputNum = v.stream().mapToInt(SubOutput::getOutputNum).sum();
            resultMap.put(subwareName, outputNum);
        });
        return AjaxResult.success(resultMap);
    }


}

