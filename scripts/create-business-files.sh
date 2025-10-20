#!/bin/bash

# Spring Boot API 项目业务文件创建脚本
# 此脚本将创建基础设施层和业务模块的所有文件

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 创建文件的函数
create_file() {
    local file_path="$1"
    local content="$2"

    # 确保目录存在
    local dir=$(dirname "$file_path")
    mkdir -p "$dir"

    # 如果文件已存在，跳过
    if [ -f "$file_path" ]; then
        print_warning "文件已存在，跳过: $file_path"
        return 0
    fi

    # 创建文件
    echo -e "$content" > "$file_path"
    print_success "创建文件: $file_path"
}

# 主函数
main() {
    print_info "开始创建业务文件..."
    echo

    # 1. 基础设施层 (infrastructure)
    print_info "1. 创建基础设施层文件..."

    # 短信服务配置
    create_file "src/main/java/top/mqxu/api/infrastructure/sms/RongLianSmsProperties.java" 'package top.mqxu.api.infrastructure.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 容联云短信配置属性
 *
 * @author mqxu
 */
@Data
@Component
@ConfigurationProperties(prefix = "ronglian")
public class RongLianSmsProperties {
    /**
     * 服务器地址
     */
    private String server;

    /**
     * 服务器端口
     */
    private String port;

    /**
     * 账户 SID
     */
    private String accountSid;

    /**
     * 认证令牌
     */
    private String authToken;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 模板 ID
     */
    private String templateId;

    /**
     * REST URL
     */
    private String restUrl;
}'

    # 短信服务接口
    create_file "src/main/java/top/mqxu/api/infrastructure/sms/SmsProvider.java" 'package top.mqxu.api.infrastructure.sms;

/**
 * 短信服务提供者接口
 *
 * @author mqxu
 */
public interface SmsProvider {

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return 发送结果
     */
    boolean sendSmsCode(String mobile, String code);
}'

    # 短信服务实现
    create_file "src/main/java/top/mqxu/api/infrastructure/sms/impl/RongLianSmsServiceImpl.java" 'package top.mqxu.api.infrastructure.sms.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.mqxu.api.infrastructure.sms.RongLianSmsProperties;
import top.mqxu.api.infrastructure.sms.SmsProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 容联云短信服务实现
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RongLianSmsServiceImpl implements SmsProvider {

    private final RongLianSmsProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public boolean sendSmsCode(String mobile, String code) {
        try {
            // 生成时间戳
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 生成签名
            String sig = calculateSignature(timestamp);

            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("accountSid", properties.getAccountSid());
            params.put("templateid", properties.getTemplateId());
            params.put("param", code);
            params.put("to", mobile);
            params.put("timestamp", timestamp);
            params.put("sig", sig);
            params.put("respDataType", "JSON");

            // 发送请求
            String url = properties.getRestUrl() + "/2013-12-26/Accounts/" + properties.getAccountSid() + "/SMS/TemplateSMS";
            HttpResponse response = HttpRequest.post(url)
                    .form(params)
                    .timeout(10000)
                    .execute();

            // 解析响应
            String body = response.body();
            JsonNode jsonNode = objectMapper.readTree(body);

            if ("000000".equals(jsonNode.get("statusCode").asText())) {
                log.info("短信验证码发送成功，手机号：{}", mobile);
                return true;
            } else {
                log.error("短信验证码发送失败，手机号：{}，错误信息：{}", mobile, jsonNode.get("statusMsg").asText());
                return false;
            }
        } catch (Exception e) {
            log.error("短信验证码发送异常，手机号：{}", mobile, e);
            return false;
        }
    }

    /**
     * 计算签名
     */
    private String calculateSignature(String timestamp) {
        String data = properties.getAccountSid() + properties.getAuthToken() + timestamp;
        HMac hmac = new HMac(HmacAlgorithm.HmacSHA1, properties.getAuthToken().getBytes());
        return hmac.digestHex(data).toUpperCase();
    }
}'

    # OSS 配置
    create_file "src/main/java/top/mqxu/api/infrastructure/oss/AliyunOssProperties.java" 'package top.mqxu.api.infrastructure.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置属性
 *
 * @author mqxu
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssProperties {
    /**
     * Endpoint
     */
    private String endpoint;

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Access Secret
     */
    private String accessSecret;

    /**
     * Bucket 名称
     */
    private String bucket;

    /**
     * 访问域名
     */
    private String host;
}'

    # OSS 服务接口
    create_file "src/main/java/top/mqxu/api/infrastructure/oss/OssService.java" 'package top.mqxu.api.infrastructure.oss;

import org.springframework.web.multipart.MultipartFile;

/**
 * OSS 服务接口
 *
 * @author mqxu
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件 URL
     */
    String uploadFile(MultipartFile file);

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     * @return 删除结果
     */
    boolean deleteFile(String objectName);
}'

    # OSS 服务实现
    create_file "src/main/java/top/mqxu/api/infrastructure/oss/impl/AliyunOssServiceImpl.java" 'package top.mqxu.api.infrastructure.oss.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.mqxu.api.infrastructure.oss.AliyunOssProperties;
import top.mqxu.api.infrastructure.oss.OssService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 阿里云 OSS 服务实现
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunOssServiceImpl implements OssService {

    private final AliyunOssProperties properties;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // 创建 OSS 客户端
            OSS ossClient = new OSSClientBuilder().build(
                    properties.getEndpoint(),
                    properties.getAccessKey(),
                    properties.getAccessSecret()
            );

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String objectName = generateObjectName(extension);

            // 设置文件元信息
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // 上传文件
            ossClient.putObject(properties.getBucket(), objectName, file.getInputStream(), metadata);

            // 关闭客户端
            ossClient.shutdown();

            // 返回文件 URL
            return properties.getHost() + "/" + objectName;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public boolean deleteFile(String objectName) {
        try {
            // 创建 OSS 客户端
            OSS ossClient = new OSSClientBuilder().build(
                    properties.getEndpoint(),
                    properties.getAccessKey(),
                    properties.getAccessSecret()
            );

            // 删除文件
            ossClient.deleteObject(properties.getBucket(), objectName);

            // 关闭客户端
            ossClient.shutdown();

            return true;
        } catch (Exception e) {
            log.error("文件删除失败，对象名称：{}", objectName, e);
            return false;
        }
    }

    /**
     * 生成对象名称
     */
    private String generateObjectName(String extension) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return String.format("uploads/%s/%s%s", date, uuid, extension);
    }
}'

    # 微信配置
    create_file "src/main/java/top/mqxu/api/infrastructure/wechat/WechatProperties.java" 'package top.mqxu.api.infrastructure.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置属性
 *
 * @author mqxu
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    /**
     * API URL
     */
    private String url;

    /**
     * App ID
     */
    private String appId;

    /**
     * App Secret
     */
    private String appSecret;
}'

    # 微信用户信息
    create_file "src/main/java/top/mqxu/api/infrastructure/wechat/WechatUserInfo.java" 'package top.mqxu.api.infrastructure.wechat;

import lombok.Data;

/**
 * 微信用户信息
 *
 * @author mqxu
 */
@Data
public class WechatUserInfo {
    /**
     * OpenID
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * UnionID（可选）
     */
    private String unionid;

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}'

    # 微信服务接口
    create_file "src/main/java/top/mqxu/api/infrastructure/wechat/WechatService.java" 'package top.mqxu.api.infrastructure.wechat;

/**
 * 微信服务接口
 *
 * @author mqxu
 */
public interface WechatService {

    /**
     * 通过 code 获取微信用户信息
     *
     * @param code 授权码
     * @return 微信用户信息
     */
    WechatUserInfo getUserInfo(String code);
}'

    # 微信服务实现
    create_file "src/main/java/top/mqxu/api/infrastructure/wechat/impl/WechatServiceImpl.java" 'package top.mqxu.api.infrastructure.wechat.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.mqxu.api.infrastructure.wechat.WechatProperties;
import top.mqxu.api.infrastructure.wechat.WechatService;
import top.mqxu.api.infrastructure.wechat.WechatUserInfo;

/**
 * 微信服务实现
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatServiceImpl implements WechatService {

    private final WechatProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public WechatUserInfo getUserInfo(String code) {
        try {
            // 构建请求 URL
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    properties.getUrl(),
                    properties.getAppId(),
                    properties.getAppSecret(),
                    code);

            // 发送 HTTP 请求
            cn.hutool.http.HttpResponse response = cn.hutool.http.HttpRequest.get(url)
                    .timeout(10000)
                    .execute();

            // 解析响应
            String body = response.body();
            JsonNode jsonNode = objectMapper.readTree(body);

            WechatUserInfo userInfo = new WechatUserInfo();

            // 检查是否有错误
            if (jsonNode.has("errcode")) {
                userInfo.setErrcode(jsonNode.get("errcode").asInt());
                userInfo.setErrmsg(jsonNode.get("errmsg").asText());
                log.error("获取微信用户信息失败：{}", userInfo.getErrmsg());
                return userInfo;
            }

            // 设置用户信息
            userInfo.setOpenid(jsonNode.get("openid").asText());
            userInfo.setSession_key(jsonNode.get("session_key").asText());

            if (jsonNode.has("unionid")) {
                userInfo.setUnionid(jsonNode.get("unionid").asText());
            }

            return userInfo;
        } catch (Exception e) {
            log.error("获取微信用户信息异常", e);
            WechatUserInfo userInfo = new WechatUserInfo();
            userInfo.setErrcode(-1);
            userInfo.setErrmsg("系统异常");
            return userInfo;
        }
    }
}'

    print_success "基础设施层文件创建完成"
    echo

    # 2. 认证模块 (auth)
    print_info "2. 创建认证模块文件..."

    # LoginDTO
    create_file "src/main/java/top/mqxu/api/module/auth/model/dto/LoginDTO.java" 'package top.mqxu.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "登录请求")
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456")
    private String password;
}'

    # SmsLoginDTO
    create_file "src/main/java/top/mqxu/api/module/auth/model/dto/SmsLoginDTO.java" 'package top.mqxu.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 短信登录 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "短信登录请求")
public class SmsLoginDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    @Schema(description = "验证码", example = "123456")
    private String code;
}'

    # WechatLoginDTO
    create_file "src/main/java/top/mqxu/api/module/auth/model/dto/WechatLoginDTO.java" 'package top.mqxu.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "微信登录请求")
public class WechatLoginDTO {

    @NotBlank(message = "授权码不能为空")
    @Schema(description = "微信授权码", example = "wx_code")
    private String code;
}'

    # BindMobileDTO
    create_file "src/main/java/top/mqxu/api/module/auth/model/dto/BindMobileDTO.java" 'package top.mqxu.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 绑定手机号 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "绑定手机号请求")
public class BindMobileDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    @Schema(description = "验证码", example = "123456")
    private String code;
}'

    # ChangeMobileDTO
    create_file "src/main/java/top/mqxu/api/module/auth/model/dto/ChangeMobileDTO.java" 'package top.mqxu.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 换绑手机号 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "换绑手机号请求")
public class ChangeMobileDTO {

    @NotBlank(message = "原手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "原手机号格式不正确")
    @Schema(description = "原手机号", example = "13800138000")
    private String oldMobile;

    @NotBlank(message = "原手机验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "原手机验证码格式不正确")
    @Schema(description = "原手机验证码", example = "123456")
    private String oldCode;

    @NotBlank(message = "新手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "新手机号格式不正确")
    @Schema(description = "新手机号", example = "13900139000")
    private String newMobile;

    @NotBlank(message = "新手机验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "新手机验证码格式不正确")
    @Schema(description = "新手机验证码", example = "654321")
    private String newCode;
}'

    # TokenVO
    create_file "src/main/java/top/mqxu/api/module/auth/model/vo/TokenVO.java" 'package top.mqxu.api.module.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Token 响应 VO
 *
 * @author mqxu
 */
@Data
@Schema(description = "Token 响应")
public class TokenVO {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "过期时间（秒）")
    private Long expiresIn;
}'

    # AuthService
    create_file "src/main/java/top/mqxu/api/module/auth/service/AuthService.java" 'package top.mqxu.api.module.auth.service;

import top.mqxu.api.module.auth.model.dto.*;
import top.mqxu.api.module.auth.model.vo.TokenVO;

/**
 * 认证服务接口
 *
 * @author mqxu
 */
public interface AuthService {

    /**
     * 用户名密码登录
     *
     * @param dto 登录请求
     * @return Token 信息
     */
    TokenVO login(LoginDTO dto);

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     */
    void sendSmsCode(String mobile);

    /**
     * 短信验证码登录
     *
     * @param dto 短信登录请求
     * @return Token 信息
     */
    TokenVO smsLogin(SmsLoginDTO dto);

    /**
     * 微信登录
     *
     * @param dto 微信登录请求
     * @return Token 信息
     */
    TokenVO wechatLogin(WechatLoginDTO dto);

    /**
     * 用户登出
     *
     * @param userId 用户ID
     */
    void logoutByUserId(Long userId);

    /**
     * 绑定手机号
     *
     * @param mobile 手机号
     * @param code   验证码
     * @param userId 用户ID
     */
    void bindMobile(String mobile, String code, Long userId);

    /**
     * 换绑手机号
     *
     * @param oldMobile 原手机号
     * @param oldCode   原手机验证码
     * @param newMobile 新手机号
     * @param newCode   新手机验证码
     * @param userId    用户ID
     */
    void changeMobile(String oldMobile, String oldCode, String newMobile, String newCode, Long userId);
}'

    # AuthController
    create_file "src/main/java/top/mqxu/api/module/auth/controller/AuthController.java" 'package top.mqxu.api.module.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.mqxu.api.common.utils.SecurityUtils;
import top.mqxu.api.common.result.Result;
import top.mqxu.api.module.auth.model.dto.*;
import top.mqxu.api.module.auth.model.vo.TokenVO;
import top.mqxu.api.module.auth.service.AuthService;

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

    @PostMapping("/send-sms-code")
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
}'

    print_success "认证模块文件创建完成"
    echo

    # 3. 用户模块 (user)
    print_info "3. 创建用户模块文件..."

    # UserEntity
    create_file "src/main/java/top/mqxu/api/module/user/model/entity/UserEntity.java" 'package top.mqxu.api.module.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.mqxu.api.common.entity.BaseEntity;

/**
 * 用户实体
 *
 * @author mqxu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "用户实体")
public class UserEntity extends BaseEntity {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String mobile;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 性别 0:未知 1:男 2:女
     */
    @Schema(description = "性别 0:未知 1:男 2:女")
    private Integer gender;

    /**
     * 微信 OpenID
     */
    @Schema(description = "微信 OpenID")
    private String wxOpenid;

    /**
     * 状态 0:禁用 1:正常
     */
    @Schema(description = "状态 0:禁用 1:正常")
    private Integer status;
}'

    # UserDTO
    create_file "src/main/java/top/mqxu/api/module/user/model/dto/UserDTO.java" 'package top.mqxu.api.module.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "用户请求参数")
public class UserDTO {

    @Schema(description = "用户ID")
    private Long id;

    @Size(min = 3, max = 20, message = "用户名长度必须在 3-20 之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Schema(description = "用户名", example = "admin")
    private String username;

    @Size(min = 6, max = 20, message = "密码长度必须在 6-20 之间")
    @Schema(description = "密码", example = "123456")
    private String password;

    @Size(max = 50, message = "真实姓名长度不能超过 50")
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过 100")
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Size(max = 50, message = "昵称长度不能超过 50")
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "性别 0:未知 1:男 2:女", example = "1")
    private Integer gender;

    @Schema(description = "状态 0:禁用 1:正常", example = "1")
    private Integer status;
}'

    # UserVO
    create_file "src/main/java/top/mqxu/api/module/user/model/vo/UserVO.java" 'package top.mqxu.api.module.user.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户 VO
 *
 * @author mqxu
 */
@Data
@Schema(description = "用户响应信息")
public class UserVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别 0:未知 1:男 2:女")
    private Integer gender;

    @Schema(description = "状态 0:禁用 1:正常")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}'

    # UserMapper
    create_file "src/main/java/top/mqxu/api/module/user/mapper/UserMapper.java" 'package top.mqxu.api.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mqxu.api.module.user.model.entity.UserEntity;

/**
 * 用户 Mapper
 *
 * @author mqxu
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}'

    # UserConvert
    create_file "src/main/java/top/mqxu/api/module/user/convert/UserConvert.java" 'package top.mqxu.api.module.user.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.mqxu.api.module.user.model.dto.UserDTO;
import top.mqxu.api.module.user.model.entity.UserEntity;
import top.mqxu.api.module.user.model.vo.UserVO;

/**
 * 用户对象转换器
 *
 * @author mqxu
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * DTO 转 Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    UserEntity toEntity(UserDTO dto);

    /**
     * Entity 转 VO
     */
    @Mapping(target = "password", ignore = true)
    UserVO toVO(UserEntity entity);
}'

    # UserService
    create_file "src/main/java/top/mqxu/api/module/user/service/UserService.java" 'package top.mqxu.api.module.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.mqxu.api.module.user.model.dto.UserDTO;
import top.mqxu.api.module.user.model.entity.UserEntity;
import top.mqxu.api.module.user.model.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author mqxu
 */
public interface UserService {

    /**
     * 分页查询用户
     *
     * @param page     分页对象
     * @param username 用户名
     * @return 用户分页列表
     */
    Page<UserVO> page(Page<UserVO> page, String username);

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO getById(Long id);

    /**
     * 保存用户
     *
     * @param dto 用户信息
     */
    void save(UserDTO dto);

    /**
     * 更新个人信息
     *
     * @param dto    用户信息
     * @param userId 用户ID
     */
    void updateProfile(UserDTO dto, Long userId);

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    UserEntity getUserByMobile(String mobile);

    /**
     * 根据微信 OpenID 查询用户
     *
     * @param wxOpenid 微信 OpenID
     * @return 用户信息
     */
    UserEntity getUserByWxOpenid(String wxOpenid);
}'

    # UserController
    create_file "src/main/java/top/mqxu/api/module/user/controller/UserController.java" 'package top.mqxu.api.module.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.mqxu.api.common.result.PageResult;
import top.mqxu.api.common.result.Result;
import top.mqxu.api.common.utils.SecurityUtils;
import top.mqxu.api.module.user.model.dto.UserDTO;
import top.mqxu.api.module.user.model.vo.UserVO;
import top.mqxu.api.module.user.service.UserService;

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
}'

    print_success "用户模块文件创建完成"
    echo

    # 4. 创建缺失的文件（这些在原项目中存在但之前脚本可能遗漏）
    print_info "4. 创建缺失的重要文件..."

    # JwtAuthenticationFilter
    create_file "src/main/java/top/mqxu/api/common/filter/JwtAuthenticationFilter.java" 'package top.mqxu.api.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.mqxu.api.common.cache.RedisCache;
import top.mqxu.api.common.cache.RedisKeys;
import top.mqxu.api.common.result.Result;
import top.mqxu.api.common.utils.JwtUtils;

import java.io.IOException;

/**
 * JWT 认证过滤器
 *
 * @author mqxu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            // 检查 Token 是否在黑名单中
            if (redisCache.hasKey(RedisKeys.JWT_BLACKLIST + token)) {
                sendErrorResponse(response, "Token 已失效，请重新登录");
                return;
            }

            try {
                Long userId = jwtUtils.parseToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.error("Token 解析失败: {}", e.getMessage());
                sendErrorResponse(response, "Token 无效");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Result<Object> result = Result.fail(401, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}'

    # CustomUserDetails
    create_file "src/main/java/top/mqxu/api/common/model/CustomUserDetails.java" 'package top.mqxu.api.common.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义用户详情
 *
 * @author mqxu
 */
@Data
public class CustomUserDetails implements UserDetails {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 账户是否未过期
     */
    private boolean accountNonExpired;

    /**
     * 账户是否未锁定
     */
    private boolean accountNonLocked;

    /**
     * 凭证是否未过期
     */
    private boolean credentialsNonExpired;

    /**
     * 账户是否启用
     */
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}'

    print_success "缺失文件创建完成"
    echo

    print_info "所有 Java 文件创建完成！"
    print_info "总共创建了以下模块："
    print_info "- 公共模块 (common)"
    print_info "- 配置类 (config)"
    print_info "- 基础设施层 (infrastructure)"
    print_info "- 认证模块 (auth)"
    print_info "- 用户模块 (user)"
    echo

    print_info "项目文件创建脚本执行完成！"
    print_info "请按照以下步骤完成项目搭建："
    print_info "1. 运行脚本创建基础配置: bash scripts/create-project.sh"
    print_info "2. 运行脚本创建 Java 文件: bash scripts/create-java-files.sh"
    print_info "3. 运行脚本创建业务文件: bash scripts/create-business-files.sh"
    print_info "4. 配置 application-secret.properties 文件"
    print_info "5. 创建数据库并执行 schema.sql"
    print_info "6. 运行项目: mvn spring-boot:run"
}

# 执行主函数
main "$@"