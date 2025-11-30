package top.wang.api.module.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.wang.api.common.cache.RedisCache;
import top.wang.api.common.cache.RedisKeys;
import top.wang.api.common.exception.ServerException;
import top.wang.api.common.utils.JwtUtils;
import top.wang.api.infrastructure.sms.SmsProvider;
import top.wang.api.infrastructure.wechat.WechatService;
import top.wang.api.infrastructure.wechat.WechatUserInfo;
import top.wang.api.module.auth.model.dto.LoginDTO;
import top.wang.api.module.auth.model.dto.SmsLoginDTO;
import top.wang.api.entity.vo.TokenVO;
import top.wang.api.module.auth.model.dto.WechatLoginDTO;
import top.wang.api.module.auth.service.AuthService;
import top.wang.api.module.user.model.entity.UserEntity;
import top.wang.api.module.user.mapper.UserMapper;

import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisCache redisCache;
    private final SmsProvider smsProvider;
    private final WechatService wechatService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * 账号密码登录
     */
    @Override
    public TokenVO login(LoginDTO dto) {
        // 查询用户
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUsername, dto.getUsername());
        UserEntity user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ServerException("用户名或密码错误");
        }
        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ServerException("用户名或密码错误");
        }
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new ServerException("账号已被禁用");
        }
        // 生成 Token
        String token = jwtUtils.generateToken(user.getId());
        // 存储 Token 到 Redis
        String tokenKey = RedisKeys.getUserTokenKey(user.getId());
        redisCache.set(tokenKey, token, jwtExpiration / 1000, TimeUnit.SECONDS);
        return new TokenVO(token, jwtExpiration / 1000);
    }

    /**
     * 短信验证码登录
     */
    @Override
    public TokenVO smsLogin(SmsLoginDTO dto) {
        // 从 Redis 获取验证码
        String key = RedisKeys.getSmsCodeKey(dto.getMobile());
        String code = redisCache.get(key, String.class);
        if (StrUtil.isBlank(code)) {
            throw new ServerException("验证码已过期");
        }
        if (!code.equals(dto.getCode())) {
            throw new ServerException("验证码错误");
        }
        // 删除验证码
        redisCache.delete(key);
        // 查询用户（根据手机号）
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getMobile, dto.getMobile());
        UserEntity user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ServerException("用户不存在");
        }
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new ServerException("账号已被禁用");
        }
        // 生成 Token
        String token = jwtUtils.generateToken(user.getId());
        // 存储 Token 到 Redis
        String tokenKey = RedisKeys.getUserTokenKey(user.getId());
        redisCache.set(tokenKey, token, jwtExpiration / 1000, TimeUnit.SECONDS);
        return new TokenVO(token, jwtExpiration / 1000);
    }

    /**
     * 微信登录
     */
    @Override
    public TokenVO wechatLogin(WechatLoginDTO dto) {
        // 调用微信接口获取 openid
        WechatUserInfo wechatUserInfo = wechatService.getUserInfo(dto.getCode());
        String openid = wechatUserInfo.getOpenid();
        // 根据 openid 查询用户
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getWxOpenid, openid);
        UserEntity user = userMapper.selectOne(wrapper);
        // 如果用户不存在，创建新用户
        if (user == null) {
            user = new UserEntity();
            user.setWxOpenid(openid);
            // 生成默认用户名
            user.setUsername("wx_" + openid.substring(0, 10));
            // 默认启用
            user.setStatus(1);
            userMapper.insert(user);
            log.info("微信登录，创建新用户，openid: {}", openid);
        }
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new ServerException("账号已被禁用");
        }
        // 生成 Token
        String token = jwtUtils.generateToken(user.getId());
        // 存储 Token 到 Redis
        String tokenKey = RedisKeys.getUserTokenKey(user.getId());
        redisCache.set(tokenKey, token, jwtExpiration / 1000, TimeUnit.SECONDS);
        return new TokenVO(token, jwtExpiration / 1000);
    }

    /**
     * 发送短信验证码
     */
    @Override
    public void sendSmsCode(String mobile) {
        // 调用短信服务发送验证码（内部会生成验证码并存储到 Redis）
        boolean success = smsProvider.sendSms(mobile);
        if (!success) {
            throw new ServerException("短信发送失败，请稍后重试");
        }
        log.info("短信验证码发送成功，手机号: {}", mobile);
    }

    /**
     * 登出
     */
    @Override
    public void logout(String token) {
        if (StrUtil.isBlank(token)) {
            return;
        }
        // 从 Token 中获取用户ID
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId != null) {
            // 删除 Redis 中的用户 Token
            String tokenKey = RedisKeys.getUserTokenKey(userId);
            redisCache.delete(tokenKey);
            log.info("用户登出成功，userId: {}", userId);
        }
    }

    @Override
    public void logoutByUserId(Long userId) {
        if (userId != null) {
            // 删除 Redis 中的用户 Token
            String tokenKey = RedisKeys.getUserTokenKey(userId);
            redisCache.delete(tokenKey);
            log.info("用户登出成功，userId: {}", userId);
        }
    }

    /**
     * 绑定手机号
     */
    @Override
    public void bindMobile(String mobile, String code, Long userId) {
        // 验证验证码
        String key = RedisKeys.getSmsCodeKey(mobile);
        String savedCode = redisCache.get(key, String.class);
        if (StrUtil.isBlank(savedCode)) {
            throw new ServerException("验证码已过期");
        }
        if (!savedCode.equals(code)) {
            throw new ServerException("验证码错误");
        }
        // 检查手机号是否已被其他用户绑定
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getMobile, mobile);
        wrapper.ne(UserEntity::getId, userId);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ServerException("该手机号已被其他用户绑定");
        }
        // 更新用户手机号
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setMobile(mobile);
        userMapper.updateById(user);
        // 删除验证码
        redisCache.delete(key);
        log.info("用户绑定手机号成功，userId: {}, mobile: {}", userId, mobile);
    }

    /**
     * 换绑手机号
     */
    @Override
    public void changeMobile(String oldMobile, String oldCode, String newMobile, String newCode, Long userId) {
        // 获取用户信息
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServerException("用户不存在");
        }
        // 验证旧手机号
        if (!oldMobile.equals(user.getMobile())) {
            throw new ServerException("旧手机号不正确");
        }
        // 验证旧手机号验证码
        String oldKey = RedisKeys.getSmsCodeKey(oldMobile);
        String oldSavedCode = redisCache.get(oldKey, String.class);
        if (StrUtil.isBlank(oldSavedCode)) {
            throw new ServerException("旧手机号验证码已过期");
        }
        if (!oldSavedCode.equals(oldCode)) {
            throw new ServerException("旧手机号验证码错误");
        }
        // 验证新手机号验证码
        String newKey = RedisKeys.getSmsCodeKey(newMobile);
        String newSavedCode = redisCache.get(newKey, String.class);
        if (StrUtil.isBlank(newSavedCode)) {
            throw new ServerException("新手机号验证码已过期");
        }
        if (!newSavedCode.equals(newCode)) {
            throw new ServerException("新手机号验证码错误");
        }
        // 检查新手机号是否已被其他用户绑定
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getMobile, newMobile);
        wrapper.ne(UserEntity::getId, userId);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ServerException("该手机号已被其他用户绑定");
        }
        // 更新手机号
        UserEntity updateUser = new UserEntity();
        updateUser.setId(userId);
        updateUser.setMobile(newMobile);
        userMapper.updateById(updateUser);

        // 删除验证码
        redisCache.delete(oldKey);
        redisCache.delete(newKey);

        log.info("用户换绑手机号成功，userId: {}, oldMobile: {}, newMobile: {}", userId, oldMobile, newMobile);
    }
}
