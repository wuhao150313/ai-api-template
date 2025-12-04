package top.wuhao.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 *
 * @author mqxu
 */
@SpringBootApplication
@MapperScan("top.wuhao.api.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("AI 演示项目启动成功！");
        System.out.println("Swagger 文档地址：http://localhost:8081/api-template/swagger-ui.html");
    }
}
