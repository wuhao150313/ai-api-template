//package top.wang.api.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import result.common.top.wuhao.api.Result;
//import utils.common.top.wuhao.api.SecurityUtils;
//import vo.entity.top.wuhao.api.ChatMessageVO;
//import dto.entity.top.wuhao.api.SendMessageDTO;
//import dto.entity.top.wuhao.api.SaveMessageDTO;
//import service.top.wuhao.api.IChatMessageManageService;
//
///**
// * 聊天消息管理控制器
// *
// * @author wang
// */
//@RestController
//@RequestMapping("/api/chat/message")
//@RequiredArgsConstructor
//@Tag(name = "聊天消息管理", description = "聊天消息相关接口")
//public class ChatMessageController {
//
//    private final IChatMessageManageService chatMessageManageService;
//
//    /**
//     * 保存用户消息
//     */
//    @PostMapping("/save/user")
//    @Operation(summary = "保存用户消息", description = "保存用户发送的消息")
//    @SecurityRequirement(name = "Authorization")
//    public Result<ChatMessageVO> saveUserMessage(@Valid @RequestBody SendMessageDTO sendMessageDTO) {
//        Long userId = SecurityUtils.getCurrentUserId();
//        ChatMessageVO message = chatMessageManageService.saveUserMessage(sendMessageDTO, userId);
//        return Result.ok(message);
//    }
//
//    /**
//     * 保存AI回答
//     */
//    @PostMapping("/save/assistant")
//    @Operation(summary = "保存AI回答", description = "保存AI回答的消息")
//    @SecurityRequirement(name = "Authorization")
//    public Result<ChatMessageVO> saveAssistantMessage(@Valid @RequestBody SaveMessageDTO saveMessageDTO) {
//        Long userId = SecurityUtils.getCurrentUserId();
//        ChatMessageVO message = chatMessageManageService.saveAssistantMessage(saveMessageDTO, userId);
//        return Result.ok(message);
//    }
//}
