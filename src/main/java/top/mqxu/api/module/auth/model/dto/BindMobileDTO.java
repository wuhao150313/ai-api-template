package top.mqxu.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 绑定手机号 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "绑定手机号请求对象")
public class BindMobileDTO implements Serializable {

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
