package top.wang.api.service.impl;

import top.wang.api.entity.ChatMessage;
import top.wang.api.mapper.ChatMessageMapper;
import top.wang.api.service.IChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天消息表 服务实现类
 * </p>
 *
 * @author wang
 * @since 2025-11-19
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements IChatMessageService {

}
