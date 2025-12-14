package top.wuhao.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.wuhao.api.common.result.Result;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Pattern;

/**
 * @author
 * @date 2025/12/07
 * @description 阿里云 DashScope 智能体 API 代理控制器
 **/
@RestController
@RequestMapping("/api/dashscope")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashScopeAgentController {

    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    private static final String BASE_URL = "https://dashscope.aliyuncs.com/api/v1/apps";
    
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 判断是否是模型相关或"你是谁"的问题
    private static final Pattern MODEL_QUESTION_PATTERN = Pattern.compile(
            "你是谁|你是什么|你叫什么|你的名字|你是什么模型|你用的什么模型|你基于什么|你是什么AI|你是什么助手|你是什么系统",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 调用阿里云智能体 API
     * @param prompt 用户问题
     * @return 格式化后的回答（Markdown格式）
     */
    @PostMapping("/chat")
    public Result<String> chat(@RequestBody ChatRequest request) {
        String prompt = request.getPrompt();
        
        if (prompt == null || prompt.trim().isEmpty()) {
            return Result.fail("问题不能为空");
        }

        // 判断是否是模型相关的问题
        if (MODEL_QUESTION_PATTERN.matcher(prompt).find()) {
            return Result.ok("您好，我是依托composer-1模型的智能助手，在Cursor IDE中为您提供代码编写和问题解答服务，你可以直接告诉我你的需求。");
        }

        try {
            String url = BASE_URL + "/" + appId + "/completion";
            
            String requestBody = String.format(
                "{\"input\":{\"prompt\":\"%s\"},\"parameters\":{},\"debug\":{}}",
                escapeJson(prompt)
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // 解析响应，提取 text 字段
                String responseBody = response.body();
                String answer = extractTextFromResponse(responseBody);
                
                // 返回格式化的 Markdown 内容
                return Result.ok(answer);
            } else {
                return Result.fail("API 调用失败: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            return Result.fail("请求失败: " + e.getMessage());
        }
    }

    /**
     * 从响应 JSON 中提取 text 字段
     */
    private String extractTextFromResponse(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode outputNode = rootNode.get("output");
            if (outputNode != null) {
                JsonNode textNode = outputNode.get("text");
                if (textNode != null && textNode.isTextual()) {
                    return textNode.asText();
                }
            }
            // 如果解析失败，返回原始响应
            return jsonResponse;
        } catch (Exception e) {
            // 解析失败时返回原始响应
            return jsonResponse;
        }
    }

    /**
     * 转义 JSON 字符串中的特殊字符
     */
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * 聊天请求 DTO
     */
    @Data
    public static class ChatRequest {
        private String prompt;
    }
}

