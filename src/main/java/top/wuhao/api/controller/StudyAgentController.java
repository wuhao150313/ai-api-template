package top.wuhao.api.controller;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.*;
import top.wuhao.api.entity.dto.RequestDTO;
import top.wuhao.api.model.AssistantResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author
 * @date 2025/11/26
 * @description 智能学习助手控制器
 **/
@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudyAgentController {

    private final ReactAgent studyAgent;

    // 存储用户的学习会话 threadId 映射
    private final Map<String, String> userThreadMap = new HashMap<>();

    /**
     * 学习助手聊天接口
     * @param request 请求DTO，包含userId和message
     * @return 助手响应
     */
    @PostMapping("/chat")
    public AssistantResponse chat(@RequestBody RequestDTO request) {
        String userId = request.getUserId();
        String message = request.getMessage();
        
        // 为每个用户生成或获取 threadId，保持会话连续性
        String threadId = userThreadMap.computeIfAbsent(userId, k -> UUID.randomUUID().toString());
        
        // 创建配置，包含threadId和用户元数据
        RunnableConfig config = RunnableConfig.builder()
                .threadId(threadId)
                .addMetadata("user_id", userId)
                .addMetadata("assistant_type", "study")
                .build();
        
        // 调用学习助手 Agent
        AssistantMessage response;
        try {
            response = studyAgent.call(message, config);
        } catch (GraphRunnerException e) {
            throw new RuntimeException("学习助手调用失败: " + e.getMessage(), e);
        }
        
        // 构建返回结果
        AssistantResponse assistantResponse = new AssistantResponse();
        assistantResponse.setUserId(userId);
        assistantResponse.setThreadId(threadId);
        assistantResponse.setAnswer(response.getText());
        assistantResponse.setType("study");
        assistantResponse.setSuggestion("继续学习，加油！💪");
        assistantResponse.setNeedsFurtherHelp(false);
        
        return assistantResponse;
    }

    /**
     * 获取学习历史记录
     * @param userId 用户ID
     * @return 历史记录信息
     */
    @GetMapping("/history/{userId}")
    public Map<String, Object> getHistory(@PathVariable String userId) {
        String threadId = userThreadMap.get(userId);
        if (threadId == null) {
            return Map.of("error", "未找到该用户的学习历史记录");
        }
        // 从MemorySaver中获取历史
        // 这里简化处理，实际应该实现完整的history接口
        return Map.of(
                "userId", userId, 
                "threadId", threadId, 
                "assistantType", "study",
                "history", "学习历史记录功能需要进一步实现"
        );
    }

    /**
     * 重置学习会话
     * @param userId 用户ID
     * @return 重置结果
     */
    @PostMapping("/reset/{userId}")
    public Map<String, Object> resetSession(@PathVariable String userId) {
        String oldThreadId = userThreadMap.remove(userId);
        String newThreadId = UUID.randomUUID().toString();
        userThreadMap.put(userId, newThreadId);
        
        return Map.of(
                "userId", userId,
                "oldThreadId", oldThreadId != null ? oldThreadId : "none",
                "newThreadId", newThreadId,
                "message", "学习会话已重置"
        );
    }
}






