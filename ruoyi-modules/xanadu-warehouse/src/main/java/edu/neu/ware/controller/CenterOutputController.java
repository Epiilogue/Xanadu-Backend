package edu.neu.ware.controller;


import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.entity.CenterOutput;
import edu.neu.ware.service.CenterInputService;
import edu.neu.ware.service.CenterOutputService;
import edu.neu.ware.vo.CenterInputVo;
import edu.neu.ware.vo.CenterOutputVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-06-02 03:42:21
 */
@RestController
@RequestMapping("/ware/centerOutput")
public class CenterOutputController {
    @Autowired
    CenterOutputService centerOutputService;

    @PostMapping("/feign/add")
    @ApiOperation("添加出库记录")
    public Boolean add(@RequestBody CenterOutputVo centerOutputVo) {
        if (centerOutputVo == null) return false;
        CenterOutput centerOutput = new CenterOutput();
        BeanUtils.copyProperties(centerOutputVo, centerOutput);
        return centerOutputService.save(centerOutput);
    }

}

