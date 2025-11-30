//package top.wang.api.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import top.wang.api.common.result.Result;
//import top.wang.api.entity.vo.UserInfoVO;
//import top.wang.api.service.IUserService;
//
///**
// * 用户管理控制器
// *
// * @author wang
// */
//@RestController
//@RequestMapping("/api/user")
//@RequiredArgsConstructor
//@Tag(name = "用户管理", description = "用户信息管理相关接口")
//public class UserManagementController {
//
//    private final IUserService userService;
//
//    @GetMapping("/{id}")
//    @Operation(summary = "根据ID获取用户信息", security = @SecurityRequirement(name = "Authorization"))
//    @PreAuthorize("hasRole('ADMIN') or @userService.getUserInfoById(#id).id == authentication.principal")
//    public Result<UserInfoVO> getUserInfoById(
//            @Parameter(description = "用户ID", required = true)
//            @PathVariable Long id) {
//        UserInfoVO userInfo = userService.getUserInfoById(id);
//        return Result.ok(userInfo);
//    }
//}