package top.wuhao.api.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 更新会话DTO
 *
 * @author wang
 */
@Data
@Schema(description = "更新会话请求")
public class UpdateSessionDTO {
    
    @Schema(description = "会话标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;
}