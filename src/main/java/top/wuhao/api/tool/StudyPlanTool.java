package top.wuhao.api.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author
 * @date 2025/11/26
 * @description 学习计划制定工具
 **/
@Component
public class StudyPlanTool implements BiFunction<String, ToolContext, List<StudyPlanTool.StudyPlan>> {

    @Override
    public List<StudyPlan> apply(
            @ToolParam(description = "学习主题或科目，如：Java编程、高等数学、英语等") String subject,
            ToolContext toolContext) {
        // 实际项目中这里应该根据主题生成个性化的学习计划
        // 这里使用模拟数据
        return Arrays.asList(
                new StudyPlan("基础理论学习", "第1-2周", "掌握核心概念和基本原理", "每天2小时"),
                new StudyPlan("实践练习", "第3-4周", "通过项目实战巩固知识", "每天3小时"),
                new StudyPlan("复习总结", "第5周", "整理笔记，查漏补缺", "每天1.5小时")
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StudyPlan {
        private String phase;
        private String duration;
        private String content;
        private String dailyTime;
    }
}






