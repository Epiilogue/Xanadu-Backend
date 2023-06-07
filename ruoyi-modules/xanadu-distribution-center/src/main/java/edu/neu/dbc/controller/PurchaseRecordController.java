package edu.neu.dbc.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/purchaseRecord")
public class PurchaseRecordController {
    //生成采购单，需要传入的为缺货单，生成采购单后，将所有涉及到的采购单状态置为已采购
    //可以修改采购数量，但是采购数量不可以小于缺货数量也不可以大于最大库存量


}

