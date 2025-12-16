package top.wuhao.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wuhao.api.common.result.Result;
import top.wuhao.api.common.utils.SecurityUtils;
import top.wuhao.api.entity.vo.ChatSessionVO;
import top.wuhao.api.entity.dto.CreateSessionDTO;
import top.wuhao.api.entity.dto.UpdateSessionDTO;
import top.wuhao.api.service.IChatSessionManageService;

import java.util.List;

/**
 * 聊天会话管理控制器
 *
 * @author wang
 */
@RestController
@RequestMapping("/api/chat/session")
@RequiredArgsConstructor
@Tag(name = "会话管理", description = "会话接口")
public class ChatSessionController {
    
    private final IChatSessionManageService chatSessionManageService;
    
    /**
     * 创建会话
     */
    @PostMapping("/create")
    @Operation(summary = "创建会话", description = "创建新的聊天会话")
    @SecurityRequirement(name = "Authorization")
    public Result<ChatSessionVO> createSession(@Valid @RequestBody CreateSessionDTO createSessionDTO) {
        Long userId = SecurityUtils.getCurrentUserId();
        ChatSessionVO session = chatSessionManageService.createSession(createSessionDTO, userId);
        return Result.ok(session);
    }
    
    /**
     * 修改会话标题
     */
    @PutMapping("/{id}/title")
    @Operation(summary = "修改会话标题", description = "修改聊天会话的标题")
    @SecurityRequirement(name = "Authorization")
    public Result<ChatSessionVO> updateSessionTitle(
            @Parameter(description = "会话ID") @PathVariable Long id,
            @Valid @RequestBody UpdateSessionDTO updateSessionDTO) {
        Long userId = SecurityUtils.getCurrentUserId();
        ChatSessionVO session = chatSessionManageService.updateSessionTitle(id, updateSessionDTO, userId);
        return Result.ok(session);
    }
    
    /**
     * 根据聊天内容动态修改会话标题
     */
    @PutMapping("/{id}/title/auto")
    @Operation(summary = "动态修改会话标题", description = "根据聊天内容自动修改会话标题")
    @SecurityRequirement(name = "Authorization")
    public Result<ChatSessionVO> updateSessionTitleByContent(
            @Parameter(description = "会话ID") @PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        ChatSessionVO session = chatSessionManageService.updateSessionTitleByContent(id, userId);
        return Result.ok(session);
    }
    
    /**
     * 获取会话详细（包含所有消息）
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取会话消息", description = "获取会话的详细信息，包含所有消息")
    @SecurityRequirement(name = "Authorization")
    public Result<ChatSessionVO> getSessionDetail(
            @Parameter(description = "会话ID") @PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        ChatSessionVO session = chatSessionManageService.getSessionDetail(id, userId);
        return Result.ok(session);
    }

    
    /**
     * 删除会话（级联删除所有消息）
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除会话", description = "删除会话，同时级联删除所有相关消息")
    @SecurityRequirement(name = "Authorization")
    public Result<Boolean> deleteSession(
            @Parameter(description = "会话ID") @PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        Boolean result = chatSessionManageService.deleteSession(id, userId);
        return Result.ok(result);
    }
    
    /**
     * 获取登录用户的会话列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取用户会话列表", description = "获取当前登录用户的所有会话列表")
    @SecurityRequirement(name = "Authorization")
    public Result<List<ChatSessionVO>> getUserSessionList() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<ChatSessionVO> sessions = chatSessionManageService.getUserSessionList(userId);
        return Result.ok(sessions);
    }

    /**
     * 更新会话收藏状态
     */
    @PutMapping("/{id}/star")
    @Operation(summary = "更新会话收藏状态", description = "设置或取消会话收藏状态")
    @SecurityRequirement(name = "Authorization")
    public Result<ChatSessionVO> updateSessionStar(
            @Parameter(description = "会话ID") @PathVariable Long id,
            @Parameter(description = "收藏标记，true=收藏，false=取消") @RequestParam Boolean star) {
        Long userId = SecurityUtils.getCurrentUserId();
        ChatSessionVO session = chatSessionManageService.updateSessionStar(id, star, userId);
        return Result.ok(session);
    }
}
