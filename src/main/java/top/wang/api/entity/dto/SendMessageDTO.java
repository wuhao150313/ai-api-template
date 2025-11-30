package top.wang.api.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 发送消息DTO
 *
 * @author wang
 */
@Data
@Schema(description = "发送消息请求")
public class SendMessageDTO {
    
    @Schema(description = "会话ID", required = true)
    @NotNull(message = "会话ID不能为空")
    private Long sessionId;
    
    @Schema(description = "聊天所用的模型", required = true)
    @NotBlank(message = "模型不能为空")
    private String modelName;
    
    @Schema(description = "提问内容", required = true)
    @NotBlank(message = "提问内容不能为空")
    private String content;
    
    @Schema(description = "是否联网搜索")
    private Boolean webSearch = false;
    
    @Schema(description = "是否深度思考")
    private Boolean deepThinking = false;
    
    @Schema(description = "是否长思考")
    private Boolean longThinking = false;
    
    @Schema(description = "附件列表")
    private List<String> attachments;
}