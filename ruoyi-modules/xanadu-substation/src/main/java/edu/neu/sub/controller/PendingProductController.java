package edu.neu.sub.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.sub.service.PendingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
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

    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(pendingProductService.list());
    }



}

