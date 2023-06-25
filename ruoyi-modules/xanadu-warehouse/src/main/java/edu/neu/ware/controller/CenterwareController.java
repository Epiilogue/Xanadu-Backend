package edu.neu.ware.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.ware.entity.Centerware;
import edu.neu.ware.service.CenterwareService;
import io.swagger.annotations.Api;
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
@RequestMapping("/ware/centerware")
@Api(tags = "中心仓库配置管理,允许设置中心仓库的相关信息")
public class CenterwareController {


    @Autowired
    private CenterwareService centerwareService;


    @PostMapping("/edit")
    public AjaxResult editCenterware(@RequestBody Centerware centerware) {
        if (centerware == null) {
            return AjaxResult.error("中心仓库信息不能为空");
        }
        //预警值不能小于100
        if (centerware.getWarnNumber() < 100) {
            return AjaxResult.error("预警值不能小于100");
        }
        //最高值必须要大于预警值两倍
        if (centerware.getMaxNumber() < centerware.getWarnNumber() * 4) {
            return AjaxResult.error("最高容量必须大于预警容量的四倍");
        }
        //更新数据库
        boolean result = centerwareService.updateById(centerware);
        if (!result) {
            return AjaxResult.error("更新失败");
        }
        return AjaxResult.success();
    }

    @GetMapping("/info")
    public AjaxResult getCenterwareInfo() {
        Centerware centerware = centerwareService.getById(1);
        if (centerware == null) {
            return AjaxResult.error("中心仓库信息不存在");
        }
        return AjaxResult.success(centerware);
    }



}

