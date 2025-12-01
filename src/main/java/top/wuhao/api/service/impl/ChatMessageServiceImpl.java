package top.wuhao.api.service.impl;

import top.wuhao.api.entity.ChatMessage;
import top.wuhao.api.mapper.ChatMessageMapper;
import top.wuhao.api.service.IChatMessageService;
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
