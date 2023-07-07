package edu.neu.ware.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.ware.entity.Centerware;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.service.CenterwareService;
import edu.neu.ware.service.SubwareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
@CacheConfig(cacheNames = "centerWareInfo")
public class CenterwareController {


    @Autowired
    private CenterwareService centerwareService;

    @Autowired
    private SubwareService subwareService;

    @PostMapping("/edit")
    @CacheEvict(allEntries = true)
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
    @Cacheable
    public AjaxResult getCenterwareInfo() {
        Centerware centerware = centerwareService.getById(1);
        if (centerware == null) {
            return AjaxResult.error("中心仓库信息不存在");
        }
        return AjaxResult.success(centerware);
    }

    @GetMapping("/listAllStations")
    @ApiOperation(value = "获取所有分站以及中心仓库信息")
    public AjaxResult listAllStations() {
        List<Subware> subList = subwareService.list();
        Centerware center = centerwareService.getById(1);
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("sub", subList);
        ajaxResult.put("center", center);
        return ajaxResult;
    }

    @ApiOperation("获取中心仓库信息列表,feign调用")
    @GetMapping("/feign/info")
    public Centerware getCenterInfo() {
        return centerwareService.getById(1);
    }
}

