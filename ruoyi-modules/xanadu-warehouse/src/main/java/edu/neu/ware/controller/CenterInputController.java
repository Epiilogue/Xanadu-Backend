package edu.neu.ware.controller;


import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.service.CenterInputService;
import edu.neu.ware.vo.CenterInputVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 */
@RestController
@RequestMapping("/ware/centerInput")
@Api(tags = "CenterInputController", description = "中心仓库入库记录管理")
public class CenterInputController {


    @Autowired
    CenterInputService centerInputService;

    @PostMapping("/feign/add")
    @ApiOperation("添加入库记录")
    public Boolean add(@RequestBody CenterInputVo centerInputVo) {
        if (centerInputVo == null) return false;
        CenterInput centerInput = new CenterInput();
        BeanUtils.copyProperties(centerInputVo, centerInput);
        return centerInputService.save(centerInput);
    }


}

