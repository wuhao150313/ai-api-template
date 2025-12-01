package top.wuhao.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程推荐服务
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseRecommendService {

    private final ChatClient.Builder chatClientBuilder;

    /**
     * 根据学习历史推荐课程
     */
    public List<String> recommendCourses(String userLevel, List<String> completedCourses) {
        ChatClient chatClient = chatClientBuilder.build();
        ListOutputConverter outputConverter = new ListOutputConverter(
                new DefaultConversionService()
        );

        String prompt = """
                请根据用户的学习水平和已完成课程，推荐 5 门适合的课程。
                用户水平：%s
                已完成课程：%s
                要求：
                1. 课程难度循序渐进
                2. 与已学课程有关联性
                3. 符合当前技术趋势
                %s
                """.formatted(userLevel, String.join("、", completedCourses), outputConverter.getFormat());
        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        log.info("AI 推荐课程：{}", response);
        assert response != null;
        return outputConverter.convert(response);
    }
}
