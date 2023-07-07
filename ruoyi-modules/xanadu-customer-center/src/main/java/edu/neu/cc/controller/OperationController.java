package edu.neu.cc.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.cc.entity.Operation;
import edu.neu.cc.service.OperationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/operation")
public class OperationController {
    //需要提供方法，按照用户id查询所有的操作记录
    @Autowired
    private OperationService operationService;

    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("查询所有的操作记录,如果携带参数则按照参数查找，否则查询所有")
    public AjaxResult list(@RequestParam(value="userId",required = false) Long userId,
                           @RequestParam(value="startTime",required = false) String startTime,
                           @RequestParam(value="endTime",required = false) String endTime,
                           @PathVariable(value = "pageNum", required = false) Integer pageNum,
                           @PathVariable(value = "pageSize", required = false) Integer pageSize) {
        Date start= DateUtil.parse(startTime); //起始时间
        Date end= DateUtil.parse(endTime);    //结束时间
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.ge(start!=null,"create_time",start)
                .le(end!=null,"create_time",end)
                .eq(userId!=null,"user_id",userId);
        if (pageNum == null || pageSize == null) {
            //查询所有
            return AjaxResult.success(operationService.list(queryWrapper));
        }
        return AjaxResult.success(operationService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }



}

