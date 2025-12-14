package top.wuhao.api.tool;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * @author
 * @date 2025/11/26
 * @description 知识点查询工具
 **/
@Component
public class KnowledgeQueryTool implements BiFunction<String, ToolContext, String> {

    @Override
    public String apply(
            @ToolParam(description = "要查询的知识点关键词，如：Spring Boot、多线程、微积分等") String keyword,
            ToolContext toolContext) {
        // 实际项目中这里应该调用知识库API或数据库查询
        // 这里使用模拟数据
        return String.format(
                "{\"keyword\": \"%s\", \"summary\": \"这是关于%s的简要说明\", \"keyPoints\": [\"核心概念1\", \"核心概念2\", \"核心概念3\"], \"examples\": \"相关示例和案例\", \"resources\": \"推荐学习资源\"}",
                keyword, keyword
        );
    }
}






