package top.wuhao.api.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息VO
 *
 * @author wang
 */
@Data
@Schema(description = "聊天消息信息")
public class ChatMessageVO {
    
    @Schema(description = "消息ID")
    private Long id;
    
    @Schema(description = "会话ID")
    private Long sessionId;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "消息角色（system-系统，user-用户，assistant-助手）")
    private String role;
    
    @Schema(description = "消息内容")
    private String content;
    
    @Schema(description = "使用的AI模型")
    private String modelName;
    
    @Schema(description = "消息的Token数量")
    private Integer tokens;
    
    @Schema(description = "是否包含思考链（0-否，1-是）")
    private Boolean hasThinking;
    
    @Schema(description = "思考链内容")
    private String thinkingContent;
    
    @Schema(description = "是否使用联网搜索（0-否，1-是）")
    private Boolean webSearch;
    
    @Schema(description = "消息状态（0-发送中，1-完成，2-失败）")
    private Boolean status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}