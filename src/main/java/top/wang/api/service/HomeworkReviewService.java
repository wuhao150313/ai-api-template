package top.wang.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import top.wang.api.model.ReviewResult;


/**
 * 作业点评服务
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkReviewService {

    private final ChatClient.Builder chatClientBuilder;

    /**
     * 自动点评作业
     */
    public ReviewResult reviewHomework(String taskDescription, String studentAnswer) {
        ChatClient chatClient = chatClientBuilder.build();
        BeanOutputConverter<ReviewResult> converter =
                new BeanOutputConverter<>(ReviewResult.class);
        String prompt = """
                你是一个专业的编程老师，请对学生提交的作业进行点评。
                作业要求：
                %s
                学生答案：
                %s
                请给出详细的点评，包括：
                1. 分数（0-100 分）
                2. 总体评价
                3. 优点
                4. 不足之处
                5. 改进建议
                %s
                """.formatted(taskDescription, studentAnswer, converter.getFormat());
        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        log.info("AI 点评结果：{}", response);
        assert response != null;
        return converter.convert(response);
    }
}
