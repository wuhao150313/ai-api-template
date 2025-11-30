package top.wang.api.service.impl;

import top.wang.api.entity.ChatSession;
import top.wang.api.mapper.ChatSessionMapper;
import top.wang.api.service.IChatSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天会话表 服务实现类
 * </p>
 *
 * @author wang
 * @since 2025-11-19
 */
@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements IChatSessionService {

}
