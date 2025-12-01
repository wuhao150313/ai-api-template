package top.wuhao.api.infrastructure.wechat;

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
     * 微信 API 地址
     */
    private String url;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;
}
