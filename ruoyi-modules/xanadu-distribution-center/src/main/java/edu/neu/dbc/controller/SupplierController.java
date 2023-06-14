package edu.neu.dbc.controller;


import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.service.SupplierService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/supplier")
public class SupplierController {

    //列表方法以及根据ID查信息

    @Autowired
    SupplierService supplierService;

    //根据ID查询名字
    @GetMapping("/feign/getSupplierNames")
    @ApiOperation("获取所有供应商的id和名字")
    public Map<Long, String> getSupplierNameById() {
        HashMap<Long, String> longStringHashMap = new HashMap<>();
        supplierService.list().forEach(
                s -> longStringHashMap.put(s.getId(), s.getName())
        );
        return longStringHashMap;
    }


}
