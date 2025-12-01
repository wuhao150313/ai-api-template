package top.wuhao.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.wuhao.api.model.ReviewResult;
import top.wuhao.api.service.HomeworkReviewService;
import top.wuhao.starter.common.result.Result;

/**
 * 作业点评接口
 *
 * @author wang
 */
@RestController
@RequestMapping("/ai/homework")
@RequiredArgsConstructor
@Tag(name = "作业点评接口")
public class HomeworkReviewController {

    private final HomeworkReviewService homeworkReviewService;

    /**
     * 作业点评接口
     */
    @PostMapping("/review")
    public Result<ReviewResult> review(@RequestBody ReviewRequest request) {
        try {
            ReviewResult result = homeworkReviewService.reviewHomework(
                    request.getTaskDescription(),
                    request.getStudentAnswer()
            );
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("AI 调用失败：" + e.getMessage());
        }
    }

    @Data
    public static class ReviewRequest {
        private String taskDescription;
        private String studentAnswer;
    }
}
