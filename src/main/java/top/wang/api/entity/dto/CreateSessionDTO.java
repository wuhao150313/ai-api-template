package top.wang.api.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建会话DTO
 *
 * @author wang
 */
@Data
@Schema(description = "创建会话请求")
public class CreateSessionDTO {
    
    @Schema(description = "会话标题")
    private String title;
    
    @Schema(description = "使用的AI模型", required = true)
    @NotBlank(message = "模型不能为空")
    private String modelName;
}