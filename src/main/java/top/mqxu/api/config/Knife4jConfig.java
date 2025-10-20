package top.mqxu.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                        .title("API Template 接口文档")
                        .version("1.0.0")
                        .summary("API Template 接口文档")
                        .description("基于 Spring Boot 3.5.5 的企业级 API 项目脚手架，提供完整的基础设施和最佳实践。")
                        .contact(new Contact()
                                .name("mqxu")
                                .email("mqxu@gmail.com")));
    }

    @Bean
    public GroupedOpenApi authApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"top.mqxu.api.module.auth"};
        return GroupedOpenApi.builder()
                .group("1")
                .displayName("Auth API")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"top.mqxu.api.module.user"};
        return GroupedOpenApi.builder()
                .group("2")
                .displayName("User API")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

}
