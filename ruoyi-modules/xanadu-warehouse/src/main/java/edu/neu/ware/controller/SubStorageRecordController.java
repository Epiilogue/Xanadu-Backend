package edu.neu.ware.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.ware.service.SubStorageRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/ware/subStorageRecord")
public class SubStorageRecordController {
    //分站记录查看
    @Autowired
    SubStorageRecordService subStorageRecordService;

    @GetMapping("/list")
    @ApiOperation(value = "分站记录查看")
    public AjaxResult list() {
        return AjaxResult.success(subStorageRecordService.list());
    }

}

