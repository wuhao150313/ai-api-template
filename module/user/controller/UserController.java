package top.wang.api.module.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import top.wang.api.common.utils.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wang.api.common.result.PageResult;
import top.wang.api.common.result.Result;
import top.wang.api.module.user.model.dto.UserDTO;
import top.wang.api.module.user.model.vo.UserVO;
import top.wang.api.module.user.service.UserService;

/**
 * 用户控制器
 *
 * @author mqxu
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表")
    public Result<PageResult<UserVO>> page(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username) {
        Page<UserVO> page = new Page<>(current, size);
        page = userService.page(page, username);
        PageResult<UserVO> pageResult = new PageResult<>(page.getRecords(), page.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询用户")
    public Result<UserVO> getById(@Parameter(description = "用户 ID") @PathVariable Long id) {
        UserVO user = userService.getById(id);
        return Result.ok(user);
    }

    @GetMapping("/user-info")
    @Operation(summary = "获取当前登录用户信息")
    public Result<UserVO> getUserInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserVO userInfo = userService.getById(userId);
        return Result.ok(userInfo);
    }

    @PostMapping
    @Operation(summary = "用户注册")
    public Result<String> register(@Validated @RequestBody UserDTO dto) {
        userService.save(dto);
        return Result.ok();
    }

    @PutMapping("/update-profile")
    @Operation(summary = "修改个人信息")
    public Result<String> updateProfile(@Validated @RequestBody UserDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateProfile(dto, userId);
        return Result.ok();
    }
}
