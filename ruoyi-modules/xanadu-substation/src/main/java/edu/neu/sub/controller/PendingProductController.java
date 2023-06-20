package edu.neu.sub.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.sub.entity.PendingProduct;
import edu.neu.sub.feign.SubwareClient;
import edu.neu.sub.service.PendingProductService;
import edu.neu.sub.vo.PendingProductVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-19 11:12:41
 */
@RestController
@RequestMapping("/sub/pendingProduct")
public class PendingProductController {

    @Autowired
    private PendingProductService pendingProductService;

    @Autowired
    private SubwareClient subwareClient;

    @GetMapping("/list")
    @ApiOperation("获取所有的待操作商品列表")
    public AjaxResult list() {
        return AjaxResult.success(pendingProductService.list());
    }

    @PostMapping("/refund/{id}/{number}")
    @ApiOperation("分库退货入库,选中记录，输入数量，将其退货出库")
    public AjaxResult refund(@PathVariable("id") Long id, @PathVariable("number") Integer number) {
        //1.根据ID拿到记录
        PendingProduct pendingProduct = pendingProductService.getById(id);
        if (pendingProduct == null)
            return AjaxResult.error("未找到该记录");
        if (pendingProduct.getDealNumber() < number)
            return AjaxResult.error("退货数量大于待处理数量");
        pendingProduct.setDealNumber(pendingProduct.getDealNumber() - number);
        boolean b = pendingProductService.updateById(pendingProduct);
        if (!b) return AjaxResult.error("更新记录失败");
        //生成退货出库记录，状态为未出库,出库类型未退货出库，需要增加退货数量
        Long subwareId = pendingProduct.getSubwareId();
        PendingProductVo pendingProductVo = new PendingProductVo();
        BeanUtils.copyProperties(pendingProduct, pendingProductVo);
        pendingProductVo.setDealNumber(number);
        AjaxResult res = subwareClient.refund(pendingProductVo);
        if(res==null||res.isError())throw new RuntimeException("退货入库登记失败");
        return AjaxResult.success("退货成功");
    }

    @PostMapping("/restore/{id}/{number}")
    @ApiOperation("重新将记录保存到可分配库存中，后续可以使用")
    public AjaxResult restore(@PathVariable("id") Long id, @PathVariable("number") Integer number) {
        //1.根据ID拿到记录
        PendingProduct pendingProduct = pendingProductService.getById(id);
        if (pendingProduct == null)
            return AjaxResult.error("未找到该记录");
        if (pendingProduct.getDealNumber() < number)
            return AjaxResult.error("重新入库数量大于待处理数量");
        pendingProduct.setDealNumber(pendingProduct.getDealNumber() - number);
        boolean b = pendingProductService.updateById(pendingProduct);
        if (!b) return AjaxResult.error("更新记录失败");
        //生成子站入库记录，状态为未入库，入库类型为退货入库
        Long subwareId = pendingProduct.getSubwareId();
        //TODO：远程调用提交给对应的分库，直接保存到数据库中并添加记录，然后生成入库记录，返回成功
        PendingProductVo pendingProductVo = new PendingProductVo();
        BeanUtils.copyProperties(pendingProduct, pendingProductVo);
        pendingProductVo.setDealNumber(number);
        AjaxResult res = subwareClient.restore(pendingProductVo);
        if(res==null||res.isError())throw new RuntimeException("重新入库失败");
        return AjaxResult.success("重新入库成功");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除记录")
    public AjaxResult delete(@PathVariable("id") Long id) {
        //只允许删除为0的记录
        PendingProduct pendingProduct = pendingProductService.getById(id);
        if (pendingProduct == null)
            return AjaxResult.error("未找到该记录");
        if (pendingProduct.getDealNumber() != 0)
            return AjaxResult.error("只允许删除待处理数量为0的记录");
        boolean b = pendingProductService.removeById(id);
        if (!b) return AjaxResult.error("删除记录失败");
        return AjaxResult.success("删除记录成功");
    }



}

