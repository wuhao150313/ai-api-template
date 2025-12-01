package top.wuhao.api.service;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.chat.MessageFormat;
import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import top.wuhao.starter.common.exception.BusinessException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * AI 问答服务
 *
 * @author wang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AIQnaService {
    private final ChatClient dashScopeChatClient;
    private final DashScopeChatOptions dashScopeChatOptions;

    private static final String DEFAULT_QUESTION = """
            你是一个专业的编程导师，请回答学生问题。
            学生问题：
            %s
            请给出详细且易懂的解答，并举例说明。
            """;

    /**
     * 普通问答
     */
    public String answerQuestionSimple(String question) {
        String prompt = DEFAULT_QUESTION.formatted(question);
        return dashScopeChatClient.prompt(prompt).call().content();
    }

    /**
     * 流式问答
     */
    public Flux<String> answerQuestionStream(String question) {
        String prompt = DEFAULT_QUESTION.formatted(question);
        return dashScopeChatClient.prompt(prompt).stream().content();
    }

    /**
     * 联网搜索
     */
    public Flux<String> webSearch(String question) {
        var searchOptions = DashScopeApi.SearchOptions.builder()
                .forcedSearch(true)
                .enableSource(true)
                .searchStrategy("pro")
                .enableCitation(true)
                .citationFormat("[<number>]")
                .build();
        dashScopeChatOptions.setSearchOptions(searchOptions);
        String prompt = DEFAULT_QUESTION.formatted(question);
        return dashScopeChatClient.prompt(prompt).stream().content();
    }

    /**
     * 切换模型
     */
    public void changeModel(String model) {
        dashScopeChatOptions.setModel(model);
    }

    public Flux<String> analyzeImageByUrl(String prompt, String imageUrl) {
        // 创建包含图片的用户消息
        List<Media> mediaList;
        try {
            mediaList = List.of(new Media(MimeTypeUtils.IMAGE_JPEG, new URI(imageUrl)));
        } catch (URISyntaxException e) {
            log.error("URL转换错误", e);
            throw new BusinessException("URI语法错误");
        }
        var message = UserMessage.builder().text(prompt).media(mediaList).build();
        // 设置消息格式为图片
        message.getMetadata().put(DashScopeApiConstants.MESSAGE_FORMAT, MessageFormat.IMAGE);
        // 启用多模态模型
        dashScopeChatOptions.setModel("qwen-vl-max-latest");
        dashScopeChatOptions.setMultiModel(true);
        dashScopeChatOptions.setVlHighResolutionImages(true);
        Prompt chatPrompt = new Prompt(message, dashScopeChatOptions);
        // 调用模型进行图片分析
        return dashScopeChatClient.prompt(chatPrompt).stream().content();
    }
}