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
 * @description 错题本管理工具
 **/
@Component
public class ErrorBookTool implements BiFunction<String, ToolContext, List<ErrorBookTool.ErrorRecord>> {

    @Override
    public List<ErrorRecord> apply(
            @ToolParam(description = "学生ID或科目名称") String studentIdOrSubject,
            ToolContext toolContext) {
        // 实际项目中这里应该从数据库查询错题记录
        // 这里使用模拟数据
        return Arrays.asList(
                new ErrorRecord("Java多线程", "线程同步问题", "2025-11-20", "已掌握"),
                new ErrorRecord("Spring Boot", "依赖注入配置", "2025-11-22", "待复习"),
                new ErrorRecord("数据结构", "二叉树遍历", "2025-11-24", "已掌握")
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ErrorRecord {
        private String subject;
        private String topic;
        private String date;
        private String status;
    }
}






