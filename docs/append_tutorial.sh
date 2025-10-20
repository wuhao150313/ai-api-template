#!/bin/bash

# 追加工具类和过滤器部分
cat >> docs/从零搭建教程.md << 'PART3'

### 6.6 工具类

**文件路径:** `src/main/java/top/mqxu/api/common/utils/JwtUtils.java`

```java
package top.mqxu.api.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 *
 * @author mqxu
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 生成密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     *
     * @param userId 用户 ID
     * @return Token
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param token Token
     * @return 用户 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 验证 Token
     *
     * @param token Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Token 剩余有效时间(毫秒)
     *
     * @param token Token
     * @return 剩余有效时间(毫秒)，如果已过期返回 null
     */
    public Long getExpiration(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long remaining = expiration.getTime() - now.getTime();
            return remaining > 0 ? remaining : null;
        } catch (Exception e) {
            return null;
        }
    }
}
```

**文件路径:** `src/main/java/top/mqxu/api/common/utils/SecurityUtils.java`

```java
package top.mqxu.api.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.mqxu.api.common.exception.ServerException;
import top.mqxu.api.common.result.ResultCode;

/**
 * Security 工具类
 * 提供获取当前认证用户信息的方法
 *
 * @author mqxu
 */
@Slf4j
public class SecurityUtils {

    /**
     * 获取当前认证的用户 ID
     *
     * @return 用户 ID
     * @throws ServerException 用户未认证时抛出异常
     */
    public static Long getCurrentUserId() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            log.warn("用户未认证，无法获取用户 ID");
            throw new ServerException(ResultCode.UNAUTHORIZED);
        }
        try {
            return (Long) authentication.getPrincipal();
        } catch (ClassCastException e) {
            log.error("获取用户 ID 失败，principal 类型不是 Long: {}", authentication.getPrincipal().getClass(), e);
            throw new ServerException(ResultCode.UNAUTHORIZED);
        }
    }

    /**
     * 获取当前认证对象
     *
     * @return Authentication 对象，可能为 null
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 检查当前是否已认证
     *
     * @return true 表示已认证，false 表示未认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getCurrentAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
```

### 6.7 过滤器

**文件路径:** `src/main/java/top/mqxu/api/common/filter/JwtAuthenticationFilter.java`

```java
package top.mqxu.api.common.filter;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.mqxu.api.common.cache.RedisCache;
import top.mqxu.api.common.cache.RedisKeys;
import top.mqxu.api.common.utils.JwtUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

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
    private final RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从请求头获取 Token
        String token = getTokenFromRequest(request);
        if (StrUtil.isNotBlank(token)) {
            // 验证 Token
            if (jwtUtils.validateToken(token)) {
                // 从 Token 中获取用户 ID
                Long userId = jwtUtils.getUserIdFromToken(token);
                // 检查 Redis 中是否存在该用户的 token
                String tokenKey = RedisKeys.getUserTokenKey(userId);
                String savedToken = redisCache.get(tokenKey, String.class);

                if (token.equals(savedToken)) {
                    // Token 有效，检查是否需要续期
                    Long remainingTime = jwtUtils.getExpiration(token);
                    // 如果剩余时间小于30分钟，自动续期
                    if (remainingTime != null && remainingTime < 30 * 60 * 1000) {
                        String newToken = jwtUtils.generateToken(userId);
                        // 更新 Redis 中的 token
                        redisCache.set(tokenKey, newToken, jwtUtils.getExpiration(newToken) / 1000, TimeUnit.SECONDS);
                        log.info("Token 自动续期成功，userId: {}", userId);
                    }

                    // Token 有效，创建认证对象,权限改为空权限列表，更适合纯 API 项目
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    Collections.emptyList()
                            );
                    // 设置到 Security 上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

**文件路径:** `src/main/java/top/mqxu/api/common/filter/LogFilter.java`

```java
package top.mqxu.api.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 日志过滤器
 *
 * @author mqxu
 */
@Slf4j
@Component
@Order(1)
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        long startTime = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String ip = getIpAddr(request);

        log.info("========================================");
        log.info("请求开始 >>> {} {}", method, uri + (queryString != null ? "?" + queryString : ""));
        log.info("客户端IP: {}", ip);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            int status = response.getStatus();

            log.info("请求结束 <<< {} {} [状态码: {}] [耗时: {}ms]", method, uri, status, executeTime);
            log.info("========================================");
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
```

PART3

echo "教程第 3 部分追加完成"
