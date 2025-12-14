package top.wuhao.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import top.wuhao.api.common.result.Result;
import top.wuhao.api.common.utils.SecurityUtils;
import top.wuhao.api.entity.dto.LoginDTO;
import top.wuhao.api.entity.vo.TokenVO;
import top.wuhao.api.service.IUserService;
import top.wuhao.api.entity.vo.UserInfoVO;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wang
 * @since 2025-11-19
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "登录管理", description = "登录、登出等接口")
@Validated
public class UserController {

    private final IUserService userService;

    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    public Result<TokenVO> login(@Validated @RequestBody LoginDTO dto) {
        TokenVO token = userService.login(dto);
        return Result.ok(token);
    }

    @PostMapping("/logout")
    @Operation(summary = "登出", security = @SecurityRequirement(name = "Authorization"))
    public Result<String> logout() {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.logoutByUserId(userId);
        return Result.ok();
    }

   @GetMapping("/user-info")
   @Operation(summary = "获取当前用户信息", security = @SecurityRequirement(name = "Authorization"))
   public Result<UserInfoVO> getCurrentUserInfo() {
       UserInfoVO userInfo = userService.getCurrentUserInfo();
       return Result.ok(userInfo);
   }
}
