package top.wuhao.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wuhao.api.entity.ChatSession;
import top.wuhao.api.entity.ChatMessage;
import top.wuhao.api.entity.vo.ChatSessionVO;
import top.wuhao.api.entity.vo.ChatMessageVO;
import top.wuhao.api.entity.dto.CreateSessionDTO;
import top.wuhao.api.entity.dto.UpdateSessionDTO;
import top.wuhao.api.mapper.ChatSessionMapper;
import top.wuhao.api.mapper.ChatMessageMapper;
import top.wuhao.api.service.IChatSessionManageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天会话管理服务实现
 *
 * @author wang
 */
@Service
@RequiredArgsConstructor
public class ChatSessionManageServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements IChatSessionManageService {
    
    private final ChatMessageMapper chatMessageMapper;
    
    @Override
    @Transactional
    public ChatSessionVO createSession(CreateSessionDTO createSessionDTO, Long userId) {
        ChatSession chatSession = new ChatSession();
        chatSession.setTitle(createSessionDTO.getTitle() != null ? createSessionDTO.getTitle() : "新会话");
        chatSession.setUserId(userId);
        chatSession.setModelName(createSessionDTO.getModelName());
        chatSession.setStatus(false); // 0-活跃
        chatSession.setStar(false);   // 默认未收藏
        chatSession.setCreateTime(LocalDateTime.now());
        chatSession.setUpdateTime(LocalDateTime.now());
        
        this.save(chatSession);
        
        return convertToVO(chatSession);
    }
    
    @Override
    @Transactional
    public ChatSessionVO updateSessionTitle(Long sessionId, UpdateSessionDTO updateSessionDTO, Long userId) {
        ChatSession chatSession = this.getById(sessionId);
        if (chatSession == null || !chatSession.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在或无权限访问");
        }
        
        chatSession.setTitle(updateSessionDTO.getTitle());
        chatSession.setUpdateTime(LocalDateTime.now());
        
        this.updateById(chatSession);
        
        return convertToVO(chatSession);
    }
    
    @Override
    @Transactional
    public ChatSessionVO updateSessionTitleByContent(Long sessionId, Long userId) {
        ChatSession chatSession = this.getById(sessionId);
        if (chatSession == null || !chatSession.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在或无权限访问");
        }
        
        // 获取会话的第一条用户消息作为标题
        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatMessage::getSessionId, sessionId)
                .eq(ChatMessage::getUserId, userId)
                .eq(ChatMessage::getRole, "user")
                .orderByAsc(ChatMessage::getCreateTime)
                .last("LIMIT 1");
        
        ChatMessage firstMessage = chatMessageMapper.selectOne(queryWrapper);
        if (firstMessage != null && firstMessage.getContent() != null) {
            String content = firstMessage.getContent();
            
            // 处理标题，移除特殊字符，限制长度
            String title = generateTitleFromContent(content);
            
            chatSession.setTitle(title);
            chatSession.setUpdateTime(LocalDateTime.now());
            
            this.updateById(chatSession);
        }
        
        return convertToVO(chatSession);
    }
    
    /**
     * 根据消息内容生成标题
     */
    private String generateTitleFromContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "新会话";
        }
        
        // 移除多余的空白字符
        String title = content.trim().replaceAll("\\s+", " ");
        
        // 移除常见的标点符号
        title = title.replaceAll("[，。！？；：''（）【】《》]", "");
        
        // 限制标题长度为10个字符
        if (title.length() > 10) {
            title = title.substring(0, 10) + "...";
        }
        
        // 如果处理后标题为空，使用默认标题
        if (title.trim().isEmpty()) {
            return "新会话";
        }
        
        return title;
    }
    
    @Override
    public ChatSessionVO getSessionDetail(Long sessionId, Long userId) {
        ChatSession chatSession = this.getById(sessionId);
        if (chatSession == null || !chatSession.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在或无权限访问");
        }
        
        ChatSessionVO sessionVO = convertToVO(chatSession);
        
        // 获取会话的所有消息
        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreateTime);
        
        List<ChatMessage> messages = chatMessageMapper.selectList(queryWrapper);
        
        List<ChatMessageVO> messageVOs = messages.stream()
                .map(this::convertMessageToVO)
                .collect(Collectors.toList());
        
        sessionVO.setMessages(messageVOs);
        
        return sessionVO;
    }
    
    @Override
    public List<ChatMessageVO> getSessionMessages(Long sessionId, Long userId) {
        // 验证会话是否属于当前用户
        ChatSession chatSession = this.getById(sessionId);
        if (chatSession == null || !chatSession.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在或无权限访问");
        }
        
        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreateTime);
        
        List<ChatMessage> messages = chatMessageMapper.selectList(queryWrapper);
        
        return messages.stream()
                .map(this::convertMessageToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Boolean deleteSession(Long sessionId, Long userId) {
        ChatSession chatSession = this.getById(sessionId);
        if (chatSession == null || !chatSession.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在或无权限访问");
        }
        
        // 删除会话相关的所有消息
        LambdaQueryWrapper<ChatMessage> messageQueryWrapper = new LambdaQueryWrapper<>();
        messageQueryWrapper.eq(ChatMessage::getSessionId, sessionId);
        chatMessageMapper.delete(messageQueryWrapper);
        
        // 删除会话
        return this.removeById(sessionId);
    }
    
    @Override
    public List<ChatSessionVO> getUserSessionList(Long userId) {
        LambdaQueryWrapper<ChatSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatSession::getUserId, userId)
                .orderByDesc(ChatSession::getStar)
                .orderByDesc(ChatSession::getUpdateTime);
        
        List<ChatSession> sessions = this.list(queryWrapper);
        
        return sessions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为VO对象
     */
    private ChatSessionVO convertToVO(ChatSession chatSession) {
        ChatSessionVO vo = new ChatSessionVO();
        BeanUtils.copyProperties(chatSession, vo);
        return vo;
    }
    
    /**
     * 转换消息为VO对象
     */
    private ChatMessageVO convertMessageToVO(ChatMessage chatMessage) {
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(chatMessage, vo);
        return vo;
    }

    @Override
    @Transactional
    public ChatSessionVO updateSessionStar(Long sessionId, Boolean star, Long userId) {
        ChatSession chatSession = this.getById(sessionId);
        if (chatSession == null || !chatSession.getUserId().equals(userId)) {
            throw new RuntimeException("会话不存在或无权限访问");
        }

        chatSession.setStar(Boolean.TRUE.equals(star));
        chatSession.setUpdateTime(LocalDateTime.now());
        this.updateById(chatSession);
        return convertToVO(chatSession);
    }
}