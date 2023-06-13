package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.entity.CenterOutput;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.service.CenterInputService;
import edu.neu.ware.service.CenterOutputService;
import edu.neu.ware.service.SubwareService;
import edu.neu.ware.vo.CenterInputVo;
import edu.neu.ware.vo.CenterOutputVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
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

    @Autowired
    SubwareService subwareService;

    @PostMapping("/feign/add")
    @ApiOperation("添加出库记录")
    public Boolean add(@RequestBody CenterOutputVo centerOutputVo) {
        if (centerOutputVo == null) return false;
        CenterOutput centerOutput = new CenterOutput();
        BeanUtils.copyProperties(centerOutputVo, centerOutput);
        centerOutput.setStatus(InputOutputStatus.NOT_OUTPUT);
        return centerOutputService.save(centerOutput);
    }


    //修改
    @PostMapping("/feign/update")
    @ApiOperation("修改出库记录")
    public AjaxResult update(@RequestBody CenterOutputVo centerOutputVo) {
        if (centerOutputVo == null) return AjaxResult.error("修改失败,参数为空");
        //拿到ID
        Long outputId = centerOutputVo.getOutputId();
        //构建查询条件，output_id为outputId
        QueryWrapper<CenterOutput> centerOutputQueryWrapper = new QueryWrapper<CenterOutput>().eq("output_id", outputId);
        //根据查询条件查询出库记录
        CenterOutput centerOutput = centerOutputService.getOne(centerOutputQueryWrapper);
        if (centerOutput == null) return AjaxResult.error("修改失败,未找到该出库记录");
        //检查一下子站是否存在
        if (centerOutputVo.getOutputType().equals(InputOutputType.DISPATCH_OUT)) {
            Subware subware = subwareService.getById(centerOutputVo.getTargetId());
            if (subware == null) return AjaxResult.error("修改失败,未找到该子站");
        } else if (centerOutputVo.getOutputType().equals(InputOutputType.RETURN_OUT)) {
            //TODO: 找供应商是否存在

        }
        //修改信息
        BeanUtils.copyProperties(centerOutputVo, centerOutput);
        //更新
        boolean update = centerOutputService.update(centerOutput, centerOutputQueryWrapper);
        if (update) return AjaxResult.success("修改成功");
        return AjaxResult.error("修改失败");
    }

    //删除
    @DeleteMapping("/feign/delete")
    @ApiOperation("删除出库记录")
    public Boolean delete(@RequestParam("outputId") Long outputId) {
        if (outputId == null) return false;
        //构建查询条件，output_id为outputId
        QueryWrapper<CenterOutput> centerOutputQueryWrapper = new QueryWrapper<CenterOutput>().eq("output_id", outputId);
        //查找一下对应的记录
        CenterOutput centerOutput = centerOutputService.getOne(centerOutputQueryWrapper);
        if (centerOutput == null) return false;
        //如果是已出库的，不能删除
        if (centerOutput.getStatus().equals(InputOutputStatus.OUTPUT)) return false;
        //与订单关联的也不能删
        if (centerOutput.getTaskId() != null) return false;
        //删除
        return centerOutputService.remove(centerOutputQueryWrapper);
    }

}

