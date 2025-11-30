package top.wang.api.infrastructure.wechat.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.wang.api.common.exception.ServerException;
import top.wang.api.infrastructure.wechat.WechatProperties;
import top.wang.api.infrastructure.wechat.WechatService;
import top.wang.api.infrastructure.wechat.WechatUserInfo;

/**
 * 微信服务实现
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatServiceImpl implements WechatService {
    private final WechatProperties wechatProperties;


    @Override
    public WechatUserInfo getUserInfo(String code) {
        try {
            // 构建请求 URL
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    wechatProperties.getUrl(), wechatProperties.getAppId(), wechatProperties.getAppSecret(), code);

            // 发送 GET 请求
            HttpResponse response = HttpRequest.get(url).execute();
            String body = response.body();

            log.info("微信登录响应: {}", body);

            // 解析响应
            WechatUserInfo userInfo = JSONUtil.toBean(body, WechatUserInfo.class);

            // 检查是否有错误
            if (userInfo.getErrcode() != null && userInfo.getErrcode() != 0) {
                log.error("微信登录失败，错误码: {}, 错误信息: {}", userInfo.getErrcode(), userInfo.getErrmsg());
                throw new ServerException("微信登录失败: " + userInfo.getErrmsg());
            }

            return userInfo;

        } catch (Exception e) {
            log.error("微信登录异常", e);
            throw new ServerException("微信登录失败，请稍后重试");
        }
    }
}
