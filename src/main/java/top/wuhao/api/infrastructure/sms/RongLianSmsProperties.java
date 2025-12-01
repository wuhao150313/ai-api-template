package top.wuhao.api.infrastructure.sms;

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
     * 端口
     */
    private String port;

    /**
     * 账号 SID
     */
    private String accountSid;

    /**
     * 认证 Token
     */
    private String authToken;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * REST API URL
     */
    private String restUrl;

    /**
     * 模板 ID
     */
    private String templateId;
}
