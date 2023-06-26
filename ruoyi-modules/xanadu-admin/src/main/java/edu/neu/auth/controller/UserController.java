package edu.neu.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.auth.entity.User;
import edu.neu.auth.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-25 10:59:21
 */
@RestController
@RequestMapping("/admin/user")

public class UserController {
    
    @Autowired
    UserService userService;
    
    @PostMapping("/add")
    @ApiOperation(value = "添加用户", notes = "添加用户")
    @CrossOrigin
    public AjaxResult addUser(@RequestBody User user) {

        boolean res = userService.save(user);
        if (!res) {
            return AjaxResult.error("创建用户失败");
        }
        return AjaxResult.success("创建用户成功", user);
    }


    @PostMapping("/update")
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @CrossOrigin
    public AjaxResult updateUser(@RequestBody User user) {

        boolean res = userService.updateById(user);
        if (!res) {
            return AjaxResult.error("修改用户失败");
        }
        return AjaxResult.success("修改用户成功", user);
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @CrossOrigin
    public AjaxResult deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        
        boolean res = userService.removeById(id);
        if (!res) {
            return AjaxResult.error("删除用户失败");
        }
        return AjaxResult.success("删除用户成功", user);
    }
    

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取用户", notes = "获取用户")
    @CrossOrigin
    public AjaxResult getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return AjaxResult.error("获取用户失败");
        }
        return AjaxResult.success("获取用户成功", user);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有用户", notes = "获取所有用户")
    @CrossOrigin
    public AjaxResult getAllUser() {
        List<User> userList = userService.list();
        if (userList == null) {
            return AjaxResult.error("获取用户列表失败");
        }
        return AjaxResult.success("获取用户列表成功", userList);
    }

    @GetMapping("/listByJob/{job}")
    @ApiOperation("通过职务获取用户列表")
    @CrossOrigin
    public AjaxResult listByJob(@PathVariable(value = "job", required = false) String job) {
        List<User> usersList;
        if (job == null) {
            usersList = null;
            return AjaxResult.success(usersList);
        }
        QueryWrapper<User> outputTypeQuery = new QueryWrapper<User>().eq("job", job);
        return AjaxResult.success(userService.list(outputTypeQuery));
    }

}

