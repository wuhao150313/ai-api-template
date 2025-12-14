package top.wuhao.api.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * @author
 * @date 2025/11/26
 * @description 学习进度跟踪工具
 **/
@Component
public class StudyProgressTool implements BiFunction<String, ToolContext, StudyProgressTool.Progress> {

    @Override
    public Progress apply(
            @ToolParam(description = "学生ID或学习主题") String studentIdOrSubject,
            ToolContext toolContext) {
        // 实际项目中这里应该从数据库查询学习进度
        // 这里使用模拟数据
        return Progress.builder()
                .subject("Java编程")
                .totalChapters(10)
                .completedChapters(6)
                .progressPercentage(60)
                .studyHours(45)
                .lastStudyTime("2025-11-25 20:30")
                .nextRecommendation("建议继续学习第7章：集合框架")
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Progress {
        private String subject;
        private Integer totalChapters;
        private Integer completedChapters;
        private Integer progressPercentage;
        private Integer studyHours;
        private String lastStudyTime;
        private String nextRecommendation;
    }
}






