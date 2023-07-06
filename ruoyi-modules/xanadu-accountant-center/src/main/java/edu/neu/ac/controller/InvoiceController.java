package edu.neu.ac.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.ac.entity.Invoice;
import edu.neu.ac.entity.Invoices;
import edu.neu.ac.mapper.InvoiceMapper;
import edu.neu.ac.service.InvoiceService;
import edu.neu.ac.service.InvoicesService;
import io.swagger.annotations.ApiOperation;
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
 * @since 2023-06-26 09:58:23
 */
@RestController
@RequestMapping("/ac/invoice")
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoicesService invoicesService;

    @GetMapping("/list")
    @ApiOperation("获取发票信息")
    @CrossOrigin
    public AjaxResult list() {
        List<Invoice> invoiceList = invoiceService.list();
        if(invoiceList == null){
            return AjaxResult.error("暂无发票信息");
        }
        return AjaxResult.success(invoiceList);
    }


    @GetMapping("/listByState/{state}")
    @ApiOperation("筛选登记和未登记的发票")
    @CrossOrigin
    public AjaxResult listByState(@PathVariable String state) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<Invoice>().eq("registration", state);
        List<Invoice> invoiceList =  invoiceService.list(queryWrapper);
        return AjaxResult.success(invoiceList);
    }


    @GetMapping("/listByReceipt")
    @ApiOperation("筛选未领用和已登记的发票")
    @CrossOrigin
    public AjaxResult listByReceipt() {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<Invoice>().eq("substation_id", "暂无信息")
                .eq("registration","已登记");
        List<Invoice> invoiceList =  invoiceService.list(queryWrapper);
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
        invoice.setTime(da);
        // 保存这批发票
        invoiceService.save(invoice);

        //根据id,开始号码和本数生成相应数量的单张发票
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<Invoice>().eq("start_number", invoice.getStartNumber());
        Invoice invoice1 = invoiceService.getOne(queryWrapper);
        System.out.println(invoice1);

        //发票号的后几位
        String Type = invoice.getStartNumber().substring(0,9);
        String No = invoice.getStartNumber().substring(10,12);

        //其他信息         
        int batch = invoice1.getBatch();
        int id = invoice1.getId();
        int total = invoice1.getTotal();

        int no = Integer.parseInt(No);
        //生成发票
        for (int i = 0; i < total; i++){
            //根据开始号码按顺序生成发票号
            Invoices invoices = new Invoices();
            invoices.setTotalid((long) id);
            invoices.setBatch(batch);
            no = no + 1;
            String number = String.format(Type + "%03d", no);
            invoices.setNumber(number);
            // 保存发票
            invoicesService.save(invoices);
            System.out.println(number);
        }

        return true;
    }

    @GetMapping("/getinvoice/{id}")
    @ApiOperation("获取发票信息")
    @CrossOrigin
    public AjaxResult getinvoice(@PathVariable long id) {
        startPage();
        Invoice invoice = invoiceService.getById(id);
        return AjaxResult.success(invoice);
    }

    @GetMapping("/getTotalId/{subId}")
    @ApiOperation("获取发票信息")
    @CrossOrigin
    public AjaxResult getTotalId(@PathVariable String subId) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<Invoice>().eq("substation_id", subId);
        Invoice invoice = invoiceService.getOne(queryWrapper);
        return AjaxResult.success(invoice.getId());
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改发票", notes = "修改发票")
    @CrossOrigin
    public AjaxResult updateInvoice(@RequestBody Invoice invoice) {
        //
        boolean res = invoiceService.updateById(invoice);
        if (!res) {
            return AjaxResult.error("修改发票失败");
        }
        return AjaxResult.success("修改成功", invoice);
    }
}

