package top.wuhao.api.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天会话VO
 *
 * @author wang
 */
@Data
@Schema(description = "聊天会话信息")
public class ChatSessionVO {
    
    @Schema(description = "会话ID")
    private Long id;
    
    @Schema(description = "会话标题")
    private String title;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "使用的AI模型")
    private String modelName;
    
    @Schema(description = "会话状态（0-活跃，1-归档）")
    private Boolean status;
    
    @Schema(description = "最后一条消息内容")
    private String lastMessage;
    
    @Schema(description = "最后一条消息时间")
    private LocalDateTime lastMessageTime;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "收藏状态（0-未收藏，1-已收藏）")
    private Boolean star;

    @Schema(description = "会话消息列表")
    private List<ChatMessageVO> messages;
}