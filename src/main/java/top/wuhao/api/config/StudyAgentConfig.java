package top.wuhao.api.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.wuhao.api.tool.ErrorBookTool;
import top.wuhao.api.tool.KnowledgeQueryTool;
import top.wuhao.api.tool.StudyPlanTool;
import top.wuhao.api.tool.StudyProgressTool;

@Configuration
public class StudyAgentConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    private static final String SYSTEM_PROMPT = """
            你是一个专业的智能学习助手"学小助"，专门帮助学生提高学习效率和成绩。

            你的职责包括：
            1. 制定个性化学习计划，帮助学生合理安排学习时间
            2. 查询知识点信息，解答学习中的疑问
            3. 跟踪学习进度，提醒学生完成学习任务
            4. 管理错题本，帮助学生查漏补缺
            5. 提供学习方法和技巧建议

            行为准则：
            - 使用鼓励和耐心的语气，像一位经验丰富的导师
            - 回答要专业准确，结合具体的学习场景
            - 主动提供学习建议和优化方案
            - 根据学生的学习情况给出个性化建议
            - 适当使用 emoji 让对话更生动友好

            工具使用规则：
            - 制定学习计划：当学生需要学习计划、时间安排时使用 createStudyPlan 工具
            - 查询知识点：当学生问知识点、概念、理论时使用 queryKnowledge 工具
            - 查询学习进度：当学生问进度、完成情况时使用 getStudyProgress 工具
            - 查询错题本：当学生问错题、复习建议时使用 getErrorBook 工具
            """;

    @Bean
    public DashScopeApi studyDashScopeApi() {
        return DashScopeApi.builder()
                .apiKey(apiKey)
                .build();
    }

    @Bean
    public DashScopeChatModel studyChatModel(DashScopeApi studyDashScopeApi) {
        return DashScopeChatModel.builder()
                .dashScopeApi(studyDashScopeApi)
                .build();
    }

    @Bean
    public ToolCallback studyPlanToolCallback(StudyPlanTool studyPlanTool) {
        return FunctionToolCallback
                .builder("createStudyPlan", studyPlanTool)
                .description("制定个性化学习计划，包括学习阶段、时间安排、学习内容等")
                .inputType(String.class)
                .build();
    }

    @Bean
    public ToolCallback knowledgeQueryToolCallback(KnowledgeQueryTool knowledgeQueryTool) {
        return FunctionToolCallback
                .builder("queryKnowledge", knowledgeQueryTool)
                .description("查询知识点信息，包括概念解释、核心要点、示例和推荐资源")
                .inputType(String.class)
                .build();
    }

    @Bean
    public ToolCallback studyProgressToolCallback(StudyProgressTool studyProgressTool) {
        return FunctionToolCallback
                .builder("getStudyProgress", studyProgressTool)
                .description("查询学习进度，包括完成章节、学习时长、进度百分比等")
                .inputType(String.class)
                .build();
    }

    @Bean
    public ToolCallback errorBookToolCallback(ErrorBookTool errorBookTool) {
        return FunctionToolCallback
                .builder("getErrorBook", errorBookTool)
                .description("查询错题本记录，包括错题科目、知识点、日期和掌握状态")
                .inputType(String.class)
                .build();
    }

    @Bean
    public ReactAgent studyAgent(DashScopeChatModel studyChatModel,
                                 ToolCallback studyPlanToolCallback,
                                 ToolCallback knowledgeQueryToolCallback,
                                 ToolCallback studyProgressToolCallback,
                                 ToolCallback errorBookToolCallback) {
        return ReactAgent.builder()
                .name("study_assistant")
                .model(studyChatModel)
                .systemPrompt(SYSTEM_PROMPT)
                .tools(studyPlanToolCallback, knowledgeQueryToolCallback, 
                       studyProgressToolCallback, errorBookToolCallback)
                .saver(new MemorySaver())
                .build();
    }
}






