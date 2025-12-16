package top.wuhao.api.service;

import top.wuhao.api.entity.vo.ChatSessionVO;
import top.wuhao.api.entity.vo.ChatMessageVO;
import top.wuhao.api.entity.dto.CreateSessionDTO;
import top.wuhao.api.entity.dto.UpdateSessionDTO;

import java.util.List;

/**
 * 聊天会话管理服务接口
 *
 * @author wang
 */
public interface IChatSessionManageService {
    
    /**
     * 创建会话
     *
     * @param createSessionDTO 创建会话请求
     * @param userId 用户ID
     * @return 会话信息
     */
    ChatSessionVO createSession(CreateSessionDTO createSessionDTO, Long userId);
    
    /**
     * 修改会话标题
     *
     * @param sessionId 会话ID
     * @param updateSessionDTO 更新会话请求
     * @param userId 用户ID
     * @return 更新后的会话信息
     */
    ChatSessionVO updateSessionTitle(Long sessionId, UpdateSessionDTO updateSessionDTO, Long userId);
    
    /**
     * 根据聊天内容动态修改会话标题
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 更新后的会话信息
     */
    ChatSessionVO updateSessionTitleByContent(Long sessionId, Long userId);
    
    /**
     * 获取会话详细（包含所有消息）
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 会话详细和消息列表
     */
    ChatSessionVO getSessionDetail(Long sessionId, Long userId);
    
    /**
     * 获取会话的所有消息
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 消息列表
     */
    List<ChatMessageVO> getSessionMessages(Long sessionId, Long userId);
    
    /**
     * 删除会话（级联删除所有消息）
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    Boolean deleteSession(Long sessionId, Long userId);
    
    /**
     * 获取登录用户的会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<ChatSessionVO> getUserSessionList(Long userId);

    /**
     * 更新会话收藏状态
     *
     * @param sessionId 会话ID
     * @param star 收藏标识（true 收藏 / false 取消）
     * @param userId 用户ID
     * @return 更新后的会话
     */
    ChatSessionVO updateSessionStar(Long sessionId, Boolean star, Long userId);
}