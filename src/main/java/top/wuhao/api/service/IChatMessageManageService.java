package top.wuhao.api.service;

import top.wuhao.api.entity.dto.SendMessageDTO;
import top.wuhao.api.entity.dto.SaveMessageDTO;
import top.wuhao.api.entity.vo.ChatMessageVO;

/**
 * 聊天消息管理服务接口
 *
 * @author wang
 */
public interface IChatMessageManageService {

    /**
     * 保存用户消息
     *
     * @param sendMessageDTO 发送消息DTO
     * @param userId 用户ID
     * @return 聊天消息VO
     */
    ChatMessageVO saveUserMessage(SendMessageDTO sendMessageDTO, Long userId);

    /**
     * 保存AI回答
     *
     * @param saveMessageDTO 保存消息DTO
     * @param userId 用户ID
     * @return 聊天消息VO
     */
    ChatMessageVO saveAssistantMessage(SaveMessageDTO saveMessageDTO, Long userId);
}