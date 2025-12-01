package top.wuhao.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Knife4j API 文档配置
 *
 * @author mqxu
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringAIAlibaba 接口文档")
                        .version("1.0.0")
                        .summary("SpringAIAlibaba 接口文档")
                        .description("Spring AI Alibaba 演示项目")
                        .contact(new Contact()
                                .name("wuhao")
                                .email("wuhao@gmail.com")))
                .components(new Components()
                        .addSecuritySchemes("Authorization", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("在此处输入JWT Token（不需要Bearer前缀）");
    }

    @Bean
    public GroupedOpenApi myApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"top.wang.api.controller"};
        return GroupedOpenApi.builder()
                .group("1")
                .displayName("SpringAIAlibaba API")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }
}
