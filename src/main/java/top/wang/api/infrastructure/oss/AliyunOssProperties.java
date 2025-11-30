package top.wang.api.infrastructure.oss;

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
     * 端点
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
     * 存储桶名称
     */
    private String bucket;

    /**
     * 访问域名
     */
    private String host;
}
