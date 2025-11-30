package top.wang.api.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 保存消息DTO
 *
 * @author wang
 */
@Data
@Schema(description = "保存消息请求")
public class SaveMessageDTO {

    @Schema(description = "会话ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会话ID不能为空")
    private Long sessionId;

    @Schema(description = "角色", example = "assistant", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色不能为空")
    private String role;

    @Schema(description = "消息内容", example = "这是AI的回答内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "消息内容不能为空")
    private String content;

    @Schema(description = "使用的模型名称", example = "gpt-3.5-turbo")
    private String modelName;

    @Schema(description = "消耗的token数量", example = "150")
    private Integer tokens;

    @Schema(description = "是否包含思考过程", example = "false")
    private Boolean hasThinking;

    @Schema(description = "思考过程内容", example = "AI的思考过程")
    private String thinkingContent;

    @Schema(description = "是否使用了联网搜索", example = "false")
    private Boolean webSearch;
}