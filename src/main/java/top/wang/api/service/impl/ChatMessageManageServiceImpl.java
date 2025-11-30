package top.wang.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wang.api.entity.ChatMessage;
import top.wang.api.entity.vo.ChatMessageVO;
import top.wang.api.entity.dto.SendMessageDTO;
import top.wang.api.entity.dto.SaveMessageDTO;
import top.wang.api.mapper.ChatMessageMapper;
import top.wang.api.service.IChatMessageManageService;

import java.time.LocalDateTime;

/**
 * 聊天消息管理服务实现
 *
 * @author wang
 */
@Service
@RequiredArgsConstructor
public class ChatMessageManageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements IChatMessageManageService {
    
    @Override
    @Transactional
    public ChatMessageVO saveUserMessage(SendMessageDTO sendMessageDTO, Long userId) {
        // 创建用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(sendMessageDTO.getSessionId());
        userMessage.setUserId(userId);
        userMessage.setRole("user");
        userMessage.setContent(sendMessageDTO.getContent());
        userMessage.setModelName(sendMessageDTO.getModelName());
        userMessage.setWebSearch(sendMessageDTO.getWebSearch());
        userMessage.setHasThinking(sendMessageDTO.getDeepThinking() || sendMessageDTO.getLongThinking());
        userMessage.setStatus(false); // 0-发送中
        userMessage.setCreateTime(LocalDateTime.now());
        userMessage.setUpdateTime(LocalDateTime.now());
        
        // 保存用户消息
        this.save(userMessage);
        
        // 返回VO对象
        return convertToVO(userMessage);
    }

    @Override
    @Transactional
    public ChatMessageVO saveAssistantMessage(SaveMessageDTO saveMessageDTO, Long userId) {
        // 创建AI回答消息
        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setSessionId(saveMessageDTO.getSessionId());
        assistantMessage.setUserId(userId);
        assistantMessage.setRole(saveMessageDTO.getRole());
        assistantMessage.setContent(saveMessageDTO.getContent());
        assistantMessage.setModelName(saveMessageDTO.getModelName());
        assistantMessage.setTokens(saveMessageDTO.getTokens());
        assistantMessage.setHasThinking(saveMessageDTO.getHasThinking());
        assistantMessage.setThinkingContent(saveMessageDTO.getThinkingContent());
        assistantMessage.setWebSearch(saveMessageDTO.getWebSearch());
        assistantMessage.setStatus(true); // 1-完成
        assistantMessage.setCreateTime(LocalDateTime.now());
        assistantMessage.setUpdateTime(LocalDateTime.now());
        
        // 保存AI回答消息
        this.save(assistantMessage);
        
        // 返回VO对象
        return convertToVO(assistantMessage);
    }
    
    /**
     * 转换为VO对象
     */
    private ChatMessageVO convertToVO(ChatMessage chatMessage) {
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(chatMessage, vo);
        return vo;
    }
}