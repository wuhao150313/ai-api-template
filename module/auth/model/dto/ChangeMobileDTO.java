package top.wang.api.module.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 换绑手机号 DTO
 *
 * @author mqxu
 */
@Data
@Schema(description = "换绑手机号请求对象")
public class ChangeMobileDTO implements Serializable {

    @Schema(description = "旧手机号")
    @NotBlank(message = "旧手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "旧手机号格式不正确")
    private String oldMobile;

    @Schema(description = "旧手机号验证码")
    @NotBlank(message = "旧手机号验证码不能为空")
    private String oldCode;

    @Schema(description = "新手机号")
    @NotBlank(message = "新手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "新手机号格式不正确")
    private String newMobile;

    @Schema(description = "新手机号验证码")
    @NotBlank(message = "新手机号验证码不能为空")
    private String newCode;
}
