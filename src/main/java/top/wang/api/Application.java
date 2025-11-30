package top.wang.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 *
 * @author mqxu
 */
@SpringBootApplication
@MapperScan("top.wang.api.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("AI 演示项目启动成功！");
        System.out.println("接口文档地址：http://localhost:8080/api-template/doc.html");
    }
}
