package top.wang.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信登录 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "微信登录")
public class WechatLoginDTO implements Serializable {

    @Schema(description = "微信授权码")
    @NotBlank(message = "微信授权码不能为空")
    private String code;
}
