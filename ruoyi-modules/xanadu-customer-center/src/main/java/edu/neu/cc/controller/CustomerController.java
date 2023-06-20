package edu.neu.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.cc.entity.Customer;
import edu.neu.cc.entity.Order;
import edu.neu.cc.service.CustomerService;
import edu.neu.cc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/customer")
public class CustomerController {

    //提供后端增删改查接口
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    /**
     * 创建新用户
     */
    @PostMapping("/create")
    public AjaxResult create(@RequestBody Customer customer) {
        //校验用户信息是否合法
        if (customer == null) {
            return AjaxResult.error("用户信息不能为空");
        }
        boolean result = customerService.save(customer);
        if (!result) {
            return AjaxResult.error("创建用户失败");
        }
        //返回结果
        return AjaxResult.success(customer);
    }

    /**
     * 删除用户,逻辑删除
     */
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        //校验用户信息是否合法
        if (id == null) {
            return AjaxResult.error("用户id不能为空");
        }
        //判断是否订购过商品
        List<Order> orderList = orderService.list(new QueryWrapper<Order>().eq("customer_id", id));
        if (orderList != null && orderList.size() != 0){
            return AjaxResult.error("该用户有订购记录，不可删除");
        }
        boolean result = customerService.removeById(id);
        if (!result) {
            return AjaxResult.error("删除用户失败");
        }
        //返回结果
        return AjaxResult.success("删除用户成功");
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody Customer customer) {
        //校验用户信息是否合法
        if (customer == null) {
            return AjaxResult.error("用户信息不能为空");
        }
        boolean result = customerService.updateById(customer);
        if (!result) {
            return AjaxResult.error("更新用户失败");
        }
        //返回结果
        return AjaxResult.success("更新用户成功");
    }

    /**
     * 查询用户信息
     *
     * @param id
     * @return AjaxResult
     */
    @GetMapping("/query/{id}")
    public AjaxResult query(@PathVariable("id") Long id) {
        //校验用户信息是否合法
        if (id == null) {
            return AjaxResult.error("用户id不能为空");
        }
        Customer customer = customerService.getById(id);
        if (customer == null) {
            return AjaxResult.error("查询用户失败");
        }
        //返回结果
        return AjaxResult.success("查询用户成功", customer);
    }

    /*
     * 用户列表查询
     * */
    @GetMapping("/list/{page}/{size}")
    public AjaxResult searchList(@PathVariable("page") Long page, @PathVariable("size") Long size,@RequestParam Map<String, String> query) {
        //设置查询条件
        String name=query.get("name");
        String telephone=query.get("telephone");
        String identityCard=query.get("identityCard");
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.like(name!=null,"name",name)
                .like(telephone!=null,"telephone",telephone)
                .like(identityCard!=null,"identity_card",identityCard);
        //分页查询客户信息
        Page<Customer> customerPage = customerService.page(new Page<>(page, size),queryWrapper);
        if (customerPage == null) {
            return AjaxResult.error("查询用户列表失败");
        }
        //返回结果
        return AjaxResult.success("查询用户列表成功", customerPage);
    }

    @GetMapping("/listAll")
    public AjaxResult listAll() {
        //查询客户信息
        return AjaxResult.success("查询用户列表成功", customerService.list());
    }

}

