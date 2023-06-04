package edu.neu.cc.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.cc.entity.NewOrder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/newOrder")
public class NewOrderController {

    @PostMapping("/create")
    @ApiOperation("创建新订单")
    @ApiParam(name = "newOrder",value = "新订单信息")
    public AjaxResult createNewOrder(@RequestBody NewOrder newOrder){
        //客户希望创建订单



    }


}

