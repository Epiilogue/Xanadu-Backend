package edu.neu.invoice.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.invoice.entity.Invoice;
import edu.neu.invoice.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import static com.ruoyi.common.core.utils.PageUtils.startPage;

/**
 * <p>
 * 发票记录 前端控制器
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-07 02:11:49
 */
@RestController
@RequestMapping("/invoice")
@Api(tags = "InvoiceController", description = "财务发票管理")
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/list")
    @ApiOperation("获取发票信息")
    @CrossOrigin
    public AjaxResult list() {
        startPage();
        List<Invoice> invoiceList = invoiceService.list();
        return AjaxResult.success(invoiceList);
    }

    @GetMapping("/listlimited")
    @ApiOperation("筛选发票信息")
    @CrossOrigin
    public AjaxResult listlimited() {
        startPage();
        List<Invoice> invoiceList = invoiceService.list();
        return AjaxResult.success(invoiceList);
    }


    @PostMapping("/register")
    @ApiOperation("改变发票登记状态")
    @CrossOrigin
    public AjaxResult registerInvoice(@RequestBody Invoice invoice) {
        //更新数据库
        boolean result = invoiceService.updateById(invoice);
        if (!result) {
            return AjaxResult.error("发票登记状态修改失败");
        }
        return AjaxResult.success();
    }



    @PostMapping("/addinvoice")
    @ApiOperation("添加发票")
    @CrossOrigin
    public Boolean add(@RequestBody Invoice invoiceVo) {
        Date da=new Date();
        SimpleDateFormat ma1=new SimpleDateFormat("yyyy 年 MM 月 dd 日 E ");
        System.out.println(ma1.format(da));
        if (invoiceVo == null) {
            return false;
        }
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceVo, invoice);
        System.out.println(invoice);
        invoice.setTime(da);
        return invoiceService.save(invoice);
    }

    @GetMapping("/getinvoice/{id}")
    @ApiOperation("获取发票信息")
    @CrossOrigin
    public AjaxResult getinvoice(@PathVariable long id) {
        startPage();
        Invoice invoice = invoiceService.getById(id);
        return AjaxResult.success(invoice);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改发票", notes = "修改发票")
    @CrossOrigin
    public AjaxResult updateInvoice(@RequestBody Invoice invoice) {

        boolean res = invoiceService.updateById(invoice);
        if (!res) {
            return AjaxResult.error("修改发票失败");
        }
        return AjaxResult.success("修改成功", invoice);
    }
}