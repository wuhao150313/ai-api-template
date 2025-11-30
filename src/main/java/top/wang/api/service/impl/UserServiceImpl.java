package top.wang.api.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.wang.api.common.cache.RedisCache;
import top.wang.api.common.cache.RedisKeys;
import top.wang.api.common.exception.ServerException;
import top.wang.api.common.utils.JwtUtils;
import top.wang.api.common.utils.SecurityUtils;
import top.wang.api.entity.User;
import top.wang.api.entity.vo.UserInfoVO;
import top.wang.api.mapper.UserMapper;
import top.wang.api.entity.dto.LoginDTO;
import top.wang.api.entity.vo.TokenVO;
import top.wang.api.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wang
 * @since 2025-11-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisCache redisCache;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * 账号密码登录
     */
    @Override
    public TokenVO login(LoginDTO dto) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne( wrapper);
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
     * 获取当前用户信息
     */
    @Override
    public UserInfoVO getCurrentUserInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        return getUserInfoById(userId);
    }

    /**
     * 根据用户ID获取用户信息
     */
    @Override
    public UserInfoVO getUserInfoById(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new ServerException("用户不存在");
        }
        
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        // 不返回密码
        return userInfoVO;
    }
}
