package edu.neu.invoice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.invoice.entity.Invoice;
import edu.neu.invoice.entity.Invoices;
import edu.neu.invoice.service.InvoicesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.utils.PageUtils.startPage;

/**
 * <p>
 * 分站发票管理 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-08 09:58:46
 */
@RestController
@RequestMapping("/invoices")
public class InvoicesController {

    @Autowired
    InvoicesService invoicesService;


    @GetMapping("/list")
    @ApiOperation("获取发票信息")
    @CrossOrigin
    public AjaxResult list() {
        List<Invoices> invoicesList = invoicesService.list();
        return AjaxResult.success(invoicesList);
    }

    @GetMapping("/getinvoicebytotalid/{totalid}")
    @ApiOperation("通过totalid获取发票信息")
    @CrossOrigin
    public AjaxResult list(@PathVariable(value = "totalid", required = false) String totalid) {
        List<Invoices> invoicesList;
        if (totalid == null) {
            invoicesList = invoicesService.list();
            return AjaxResult.success(invoicesList);
        }
        QueryWrapper<Invoices> outputTypeQuery = new QueryWrapper<Invoices>().eq("totalid", totalid);
        return AjaxResult.success(invoicesService.list(outputTypeQuery));
    }

    @GetMapping("/getinvoice/{id}")
    @ApiOperation("通过id获取发票信息")
    @CrossOrigin
    public AjaxResult getInvoices(@PathVariable long id) {
        Invoices invoices = invoicesService.getById(id);
        return AjaxResult.success(invoices);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改发票", notes = "修改发票")
    @CrossOrigin
    public AjaxResult updateInvoice(@RequestBody Invoices invoices) {

        boolean res = invoicesService.updateById(invoices);
        if (!res) {
            return AjaxResult.error("修改发票失败");
        }
        return AjaxResult.success("修改成功", invoices);
    }
}

