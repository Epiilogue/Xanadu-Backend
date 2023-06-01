package edu.neu.cc.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 10:32:09
 */
@RestController
@RequestMapping("/cc/stockout")
@Api(tags = "提交缺货信息")
public class StockoutController {

}

