package top.wuhao.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天消息表
 * </p>
 *
 * @author wang
 * @since 2025-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_message")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 消息角色（system-系统，user-用户，assistant-助手）
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 使用的AI模型
     */
    private String modelName;

    /**
     * 消息的Token数量
     */
    private Integer tokens;

    /**
     * 是否包含思考链（0-否，1-是）
     */
    private Boolean hasThinking;

    /**
     * 思考链内容
     */
    private String thinkingContent;

    /**
     * 是否使用联网搜索（0-否，1-是）
     */
    private Boolean webSearch;

    /**
     * 消息状态（0-发送中，1-完成，2-失败）
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除（0-未删除，1-已删除）
     */
    private Boolean deleted;


}
