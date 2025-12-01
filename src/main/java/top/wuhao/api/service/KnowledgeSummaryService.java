package top.wuhao.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * 知识总结服务
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeSummaryService {
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 总结课程要点
     */
    public String summarizeCourse(String courseContent) {
        ChatClient chatClient = chatClientBuilder.build();
        String prompt = """
                请总结以下课程内容的核心要点。
                课程内容：
                %s
                要求：
                1. 提炼 3-5 个核心知识点
                2. 每个知识点用简洁的语言描述
                3. 标注重要程度（⭐⭐⭐）
                4. 给出学习建议
                """.formatted(courseContent);
        String summary = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        log.info("课程总结：{}", summary);
        return summary;
    }

    /**
     * 生成思维导图结构
     */
    public String generateMindMap(String courseContent) {
        ChatClient chatClient = chatClientBuilder.build();
        String prompt = """
                请将以下课程内容转换为思维导图结构（Markdown 格式）。
                课程内容：
                %s
                要求：
                1. 使用 Markdown 的层级列表格式
                2. 主题明确，层次清晰
                3. 包含关键知识点和子知识点
                """.formatted(courseContent);
        String mindMap = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        log.info("思维导图：{}", mindMap);
        return mindMap;
    }
}
