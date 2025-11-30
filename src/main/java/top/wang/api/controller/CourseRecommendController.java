package top.wang.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.wang.api.service.CourseRecommendService;
import top.wuhao.starter.common.result.Result;

import java.util.List;

/**
 * 课程推荐接口
 *
 * @author wang
 */
@RestController
@RequestMapping("/ai/recommend")
@RequiredArgsConstructor
@Tag(name = "课程推荐接口")
public class CourseRecommendController {
    private final CourseRecommendService courseRecommendService;

    /**
     * 课程推荐接口
     */
    @PostMapping("/courses")
    public Result<List<String>> recommend(@RequestBody RecommendRequest request) {
        List<String> courses = courseRecommendService.recommendCourses(
                request.getUserLevel(),
                request.getCompletedCourses());
        return Result.success(courses);
    }

    @Data
    public static class RecommendRequest {
        private String userLevel;
        private List<String> completedCourses;
    }
}
