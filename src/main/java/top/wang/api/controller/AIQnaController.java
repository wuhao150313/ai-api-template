package top.wang.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.wang.api.service.AIQnaService;

/**
 * AI 问答接口
 *
 * @author wang
 */
@RestController
@RequestMapping("/ai/qna")
@RequiredArgsConstructor
@Tag(name = "提问接口")
public class AIQnaController {
    private static final String DEFAULT_QUESTION = "你是谁";
    private static final String DEFAULT_IMAGE_URL = "https://wang-oss-study.oss-cn-nanjing.aliyuncs.com/b_a4367029f3600f2e1a62a3f8dcc733dc.jpg";
    private static final String DEFAULT_IMAGE_PROMPT = "请分析图片内容";

    private final AIQnaService aiQnaService;

    /**
     * 提问接口（普通响应）
     */
    @GetMapping("/ask")
    @Operation(summary = "普通问答接口", description = "返回 AI 回答内容")
    public String ask(@RequestParam(defaultValue = DEFAULT_QUESTION) String question) {
        return aiQnaService.answerQuestionSimple(question);
    }

    /**
     * 提问接口（流式响应）
     */
    @GetMapping("/ask/stream")
    @Operation(summary = "流式问答接口", description = "实时流式返回 AI 回答内容")
    public Flux<String> askStream(@RequestParam(defaultValue = DEFAULT_QUESTION) String question) {
        return aiQnaService.answerQuestionStream(question);
    }

    /**
     * 联网搜索
     */
    @GetMapping("/web-search")
    @Operation(summary = "联网搜索功能", description = "联网搜索功能")
    public Flux<String> webSearch(@RequestParam(defaultValue = DEFAULT_QUESTION) String question) {
        return aiQnaService.webSearch(question);
    }

    /**
     * 切换模型
     */
    @GetMapping("/switch-model")
    @Operation(summary = "切换模型", description = "切换模型")
    public String switchModel(@RequestParam(defaultValue = "qwen-turbo") String model) {
        aiQnaService.changeModel(model);
        return "切换模型成功";
    }

    /**
     * 图片分析接口 - 通过 URL
     */
    @GetMapping("/image/analyze/url")
    @Operation(summary = "图片分析接口 - 通过 URL", description = "图片分析接口 - 通过 URL")
    public Flux<String> analyzeImageByUrl(@RequestParam(defaultValue = DEFAULT_IMAGE_PROMPT) String prompt, @RequestParam(defaultValue = DEFAULT_IMAGE_URL) String imageUrl) {
        return aiQnaService.analyzeImageByUrl(prompt, imageUrl);
    }
}