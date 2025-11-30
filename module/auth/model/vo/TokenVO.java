package top.wang.api.module.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Token 响应 VO
 *
 * @author mqxu
 */
@Data
@Schema(description = "Token 响应")
public class TokenVO implements Serializable {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "过期时间（秒）")
    private Long expiresIn;

    public TokenVO(String token, Long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
