package top.wuhao.api.infrastructure.sms.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.wuhao.api.common.cache.RedisCache;
import top.wuhao.api.common.cache.RedisKeys;
import top.wuhao.api.infrastructure.sms.RongLianSmsProperties;
import top.wuhao.api.infrastructure.sms.SmsProvider;

import java.util.concurrent.TimeUnit;

/**
 * 容联云短信服务实现
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RongLianSmsServiceImpl implements SmsProvider {
    private final RongLianSmsProperties rongLianSmsProperties;
    private final RedisCache redisCache;

    @Override
    public boolean sendSms(String mobile) {
        // 生成 4 位验证码
        String code = RandomUtil.randomNumbers(4);

        // 开发环境不真正发送短信，避免触顶
        log.info("【模拟发送】手机号: {}, 验证码: {}", mobile, code);

        // 生产环境使用真实发送
//        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
//        sdk.init(rongLianSmsProperties.getServer(), rongLianSmsProperties.getPort());
//        sdk.setAccount(rongLianSmsProperties.getAccountSid(), rongLianSmsProperties.getAuthToken());
//        sdk.setAppId(rongLianSmsProperties.getAppId());
//        sdk.setBodyType(BodyType.Type_JSON);
//        String[] datas = {String.valueOf(code), "1"};
//        // 发送短信
//        HashMap<String, Object> result = sdk.sendTemplateSMS(mobile, rongLianSmsProperties.getTemplateId(), datas, "1234", UUID.randomUUID().toString());
//        String statusCode = result.get("statusCode").toString();
//        if (!"000000".equals(statusCode)) {
//            log.error("短信发送失败，手机号: {}, 错误码: {}, 错误信息: {}", mobile, statusCode, statusCode);
//            return false;
//        }
//        log.info("短信发送成功，手机号: {}, 验证码: {}", mobile, code);

        // 存入 Redis，有效期 5 分钟
        String codeKey = RedisKeys.getSmsCodeKey(mobile);
        redisCache.set(codeKey, code, 5, TimeUnit.MINUTES);

        return true;
    }
}