package edu.neu.ware.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 *
 * 主要提供的是分库的出库记录查看
 * 具体逻辑为：分站填写回执，登记退货，分库可查看退货记录
 * 分库可以选择退货，然后填写商品数量，如果商品数量小于退货数量，则剩余商品重新入库，作为可分配商品信息
 */
@RestController
@RequestMapping("/ware/subOutput")
public class SubOutputController {



}

