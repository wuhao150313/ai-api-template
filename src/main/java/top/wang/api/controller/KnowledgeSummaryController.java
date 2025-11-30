package top.wang.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.wang.api.service.KnowledgeSummaryService;
import top.wuhao.starter.common.result.Result;

/**
 * 知识总结接口
 *
 * @author wang
 */
@RestController
@RequestMapping("/ai/summary")
@RequiredArgsConstructor
@Tag(name = "课程总结接口")
public class KnowledgeSummaryController {

    private final KnowledgeSummaryService knowledgeSummaryService;

    /**
     * 课程总结接口
     */
    @PostMapping("/course")
    public Result<String> summarizeCourse(@RequestBody SummaryRequest request) {
        try {
            String summary = knowledgeSummaryService.summarizeCourse(
                    request.getCourseContent()
            );
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("AI 调用失败：" + e.getMessage());
        }
    }

    /**
     * 思维导图生成接口
     */
    @PostMapping("/mindmap")
    public Result<String> generateMindMap(@RequestBody SummaryRequest request) {
        try {
            String mindMap = knowledgeSummaryService.generateMindMap(
                    request.getCourseContent()
            );
            return Result.success(mindMap);
        } catch (Exception e) {
            return Result.error("AI 调用失败：" + e.getMessage());
        }
    }

    @Data
    public static class SummaryRequest {
        private String courseContent;
    }
}
