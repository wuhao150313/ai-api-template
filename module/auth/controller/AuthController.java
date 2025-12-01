package top.wang.api.module.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import top.wang.api.common.utils.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wang.api.common.result.Result;
import top.wang.api.entity.vo.TokenVO;
import top.wang.api.module.auth.service.AuthService;

/**
 * 认证控制器
 *
 * @author mqxu
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "登录、登出等认证相关接口")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    public Result<TokenVO> login(@Validated @RequestBody LoginDTO dto) {
        TokenVO token = authService.login(dto);
        return Result.ok(token);
    }

    @GetMapping("/send-sms-code")
    @Operation(summary = "发送短信验证码")
    public Result<String> sendSmsCode(
            @Parameter(description = "手机号")
            @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
            @RequestParam String mobile) {
        authService.sendSmsCode(mobile);
        return Result.ok();
    }

    @PostMapping("/sms-login")
    @Operation(summary = "短信验证码登录")
    public Result<TokenVO> smsLogin(@Validated @RequestBody SmsLoginDTO dto) {
        TokenVO token = authService.smsLogin(dto);
        return Result.ok(token);
    }

    @PostMapping("/wechat-login")
    @Operation(summary = "微信登录")
    public Result<TokenVO> wechatLogin(@Validated @RequestBody WechatLoginDTO dto) {
        TokenVO token = authService.wechatLogin(dto);
        return Result.ok(token);
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public Result<String> logout() {
        Long userId = SecurityUtils.getCurrentUserId();
        authService.logoutByUserId(userId);
        return Result.ok();
    }

    @PostMapping("/bind-mobile")
    @Operation(summary = "绑定手机号")
    public Result<String> bindMobile(@Validated @RequestBody BindMobileDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        authService.bindMobile(dto.getMobile(), dto.getCode(), userId);
        return Result.ok();
    }

    @PostMapping("/change-mobile")
    @Operation(summary = "换绑手机号")
    public Result<String> changeMobile(@Validated @RequestBody ChangeMobileDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        authService.changeMobile(dto.getOldMobile(), dto.getOldCode(),
                dto.getNewMobile(), dto.getNewCode(), userId);
        return Result.ok();
    }
}
