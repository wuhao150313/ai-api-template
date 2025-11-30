package top.wang.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wang.api.common.result.Result;
import top.wang.api.entity.vo.AIModelVO;
import top.wang.api.service.IAIModelService;

import java.util.List;

/**
 * AI模型管理控制器
 *
 * @author wang
 */
@RestController
@RequestMapping("/api/ai/model")
@RequiredArgsConstructor
@Tag(name = "模型管理", description = "模型接口")
public class AIModelController {

    private final IAIModelService aiModelService;

    /**
     * 获取所有可用模型列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取模型列表", security = @SecurityRequirement(name = "Authorization"))
    public Result<List<AIModelVO>> getModelList() {
        List<AIModelVO> modelList = aiModelService.getModelList();
        return Result.ok(modelList);
    }

    /**
     * 获取当前使用的模型
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前模型", security = @SecurityRequirement(name = "Authorization"))
    public Result<AIModelVO> getCurrentModel() {
        AIModelVO currentModel = aiModelService.getCurrentModel();
        return Result.ok(currentModel);
    }

    /**
     * 切换模型
     */
    @PostMapping("/switch")
    @Operation(summary = "切换模型", security = @SecurityRequirement(name = "Authorization"))
    public Result<AIModelVO> switchModel(
            @Parameter(description = "模型名称", required = true)
            @RequestParam String modelName) {
        AIModelVO newModel = aiModelService.switchModel(modelName);
        return Result.ok(newModel);
    }
}