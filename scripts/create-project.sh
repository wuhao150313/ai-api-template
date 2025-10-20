#!/bin/bash

# Spring Boot API 项目文件创建脚本
# 此脚本将根据教程内容创建完整的项目结构和所有文件

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 创建目录的函数
create_dir() {
    if [ ! -d "$1" ]; then
        mkdir -p "$1"
        print_info "创建目录: $1"
    else
        print_warning "目录已存在: $1"
    fi
}

# 创建文件的函数
create_file() {
    local file_path="$1"
    local content="$2"

    # 确保目录存在
    local dir=$(dirname "$file_path")
    create_dir "$dir"

    # 如果文件已存在，跳过
    if [ -f "$file_path" ]; then
        print_warning "文件已存在，跳过: $file_path"
        return 0
    fi

    # 创建文件
    echo -e "$content" > "$file_path"
    print_success "创建文件: $file_path"
}

# 检查是否在项目根目录
check_project_root() {
    if [ -f "pom.xml" ] && [ -d "src" ]; then
        print_warning "检测到已存在的项目，将在当前目录创建/覆盖文件"
        return 0
    else
        print_info "将在当前目录创建新项目"
        return 1
    fi
}

# 主函数
main() {
    print_info "开始创建 Spring Boot API 项目文件..."
    print_info "项目将创建在当前目录: $(pwd)"

    echo
    read -p "是否继续？(y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "操作已取消"
        exit 0
    fi

    # 检查项目根目录
    project_exists=false
    if check_project_root; then
        project_exists=true
    fi

    print_info "开始创建项目结构..."
    echo

    # 1. 创建目录结构
    print_info "1. 创建项目目录结构..."

    # 主目录
    create_dir "src/main/java/top/mqxu/api"
    create_dir "src/main/resources"
    create_dir "src/test/java"
    create_dir "sql"
    create_dir "logs"
    create_dir "scripts"

    # 公共模块目录
    create_dir "src/main/java/top/mqxu/api/common/cache"
    create_dir "src/main/java/top/mqxu/api/common/entity"
    create_dir "src/main/java/top/mqxu/api/common/enums"
    create_dir "src/main/java/top/mqxu/api/common/exception"
    create_dir "src/main/java/top/mqxu/api/common/filter"
    create_dir "src/main/java/top/mqxu/api/common/handler"
    create_dir "src/main/java/top/mqxu/api/common/result"
    create_dir "src/main/java/top/mqxu/api/common/utils"

    # 配置类目录
    create_dir "src/main/java/top/mqxu/api/config"

    # 基础设施层目录
    create_dir "src/main/java/top/mqxu/api/infrastructure/oss"
    create_dir "src/main/java/top/mqxu/api/infrastructure/sms"
    create_dir "src/main/java/top/mqxu/api/infrastructure/wechat"

    # 业务模块目录
    create_dir "src/main/java/top/mqxu/api/module"
    create_dir "src/main/java/top/mqxu/api/module/auth/model/dto"
    create_dir "src/main/java/top/mqxu/api/module/auth/model/vo"
    create_dir "src/main/java/top/mqxu/api/module/auth/service/impl"
    create_dir "src/main/java/top/mqxu/api/module/auth/controller"

    create_dir "src/main/java/top/mqxu/api/module/user/model/dto"
    create_dir "src/main/java/top/mqxu/api/module/user/model/vo"
    create_dir "src/main/java/top/mqxu/api/module/user/model/entity"
    create_dir "src/main/java/top/mqxu/api/module/user/mapper"
    create_dir "src/main/java/top/mqxu/api/module/user/service/impl"
    create_dir "src/main/java/top/mqxu/api/module/user/controller"
    create_dir "src/main/java/top/mqxu/api/module/user/convert"

    create_dir "src/main/resources/mapper"

    print_success "目录结构创建完成"
    echo

    # 2. 创建 pom.xml
    print_info "2. 创建 pom.xml..."
    create_file "pom.xml" '<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.5</version>
        <relativePath/>
    </parent>

    <groupId>top.mqxu</groupId>
    <artifactId>api-template</artifactId>
    <version>1.0.0</version>
    <name>api-template</name>
    <description>Spring Boot API 项目脚手架</description>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <mybatis-plus.version>3.5.7</mybatis-plus.version>
        <hutool.version>5.8.40</hutool.version>
        <knife4j.version>4.5.0</knife4j.version>
        <springdoc.version>2.8.8</springdoc.version>
        <druid.version>1.2.25</druid.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <jjwt.version>0.12.6</jjwt.version>
        <httpclient.version>4.5.14</httpclient.version>
        <cloopen.sms.version>1.0.4</cloopen.sms.version>
        <aliyun-oss.version>3.18.1</aliyun-oss.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-3-starter</artifactId>
            <version>${druid.version}</version>
        </dependency>

        <!-- API Documentation -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>${knife4j.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.springdoc</groupId>
                    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <!-- Utils -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${mapstruct.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>${httpclient.version}</version>
        </dependency>
        <dependency>
            <groupId>com.cloopen</groupId>
            <artifactId>java-sms-sdk</artifactId>
            <version>${cloopen.sms.version}</version>
        </dependency>
        <dependency>
            <groupId>com.aliyun.oss</groupId>
            <artifactId>aliyun-sdk-oss</artifactId>
            <version>${aliyun-oss.version}</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>api-template</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>'

    # 3. 创建启动类
    print_info "3. 创建启动类..."
    create_file "src/main/java/top/mqxu/api/Application.java" 'package top.mqxu.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 *
 * @author mqxu
 */
@SpringBootApplication
@MapperScan("top.mqxu.api.**.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}'

    # 4. 创建配置文件
    print_info "4. 创建配置文件..."

    # application.yml
    create_file "src/main/resources/application.yml" '# Tomcat
server:
  tomcat:
    uri-encoding: UTF-8
    threads:
      max: 1000
      min-spare: 30
  port: 8083
  servlet:
    context-path: /
    session:
      cookie:
        http-only: true

# Knife4j 配置
knife4j:
  enable: false
  setting:
    custom-code: 500
    enable-footer-custom: false

spring:
  config:
    import:
      - optional:classpath:application-secret.properties
  profiles:
    active: dev
  application:
    name: api-template
  jackson:
    time-zone: GMT+8
  servlet:
    multipart:
      max-file-size: 1024MB
      max-request-size: 1024MB

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  typeAliasesPackage: top.mqxu.api.module.*.entity
  global-config:
    db-config:
      id-type: AUTO
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  # 原生配置
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true
    jdbc-type-for-null: '\''null'\''
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  configuration-properties:
    prefix:
    blobType: BLOB
    boolValue: TRUE

# JWT 配置（从 application-secret.properties 读取）
jwt:
  secret: ${jwt.secret}
  expiration: ${jwt.expiration}

# 容联云短信配置（从 application-secret.properties 读取）
ronglian:
  server: ${ronglian.server}
  port: ${ronglian.port}
  account-sid: ${ronglian.account-sid}
  auth-token: ${ronglian.auth-token}
  app-id: ${ronglian.app-id}
  rest-url: https://${ronglian.server}:${ronglian.port}
  template-id: ${ronglian.template-id}

# 阿里云 OSS 配置（从 application-secret.properties 读取）
aliyun:
  oss:
    endpoint: ${aliyun.oss.endpoint}
    access-key: ${aliyun.oss.access-key}
    access-secret: ${aliyun.oss.access-secret}
    bucket: ${aliyun.oss.bucket}
    host: ${aliyun.oss.bucket}.${aliyun.oss.endpoint}

# 微信 API 配置（从 application-secret.properties 读取）
wechat:
  url: https://api.weixin.qq.com/sns/jscode2session
  app-id: ${wechat.app-id}
  app-secret: ${wechat.app-secret}'

    # application-dev.yml
    create_file "src/main/resources/application-dev.yml" '# 开发环境配置
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/api_template?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&allowPublicKeyRetrieval=true
    username: root
    password: 123456
    hikari:
      # 连接池配置
      maximum-pool-size: 50
      minimum-idle: 10
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      # 连接测试配置
      connection-test-query: SELECT 1
      validation-timeout: 5000
      # 连接命名（便于调试）
      pool-name: HikariCP-Production

# 开发环境日志配置（在 logback-spring.xml 中管理）'

    # application-prod.yml
    create_file "src/main/resources/application-prod.yml" '# 生产环境配置
spring:
  data:
    redis:
      host: ${REDIS_HOST:redis}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}
      database: 0
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${MYSQL_HOST:mysql}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:api_template}?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&allowPublicKeyRetrieval=true
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:123456}
    hikari:
      # 连接池配置
      maximum-pool-size: 50
      minimum-idle: 10
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      # 连接测试配置
      connection-test-query: SELECT 1
      validation-timeout: 5000
      # 连接命名（便于调试）
      pool-name: HikariCP-Production'

    # application-secret.properties.example
    create_file "src/main/resources/application-secret.properties.example" '# 应用敏感配置示例
# 复制此文件为 application-secret.properties 并填入实际值
# 此文件包含敏感信息，请不要提交到代码仓库

# ========================================
# JWT 配置
# ========================================
jwt.secret=your-256-bit-secret-key-for-jwt-token-generation-min-32-chars
jwt.expiration=86400000

# ========================================
# 短信服务配置
# ========================================
ronglian.server=app.cloopen.com
ronglian.port=8883
ronglian.account-sid=your_account_sid
ronglian.auth-token=your_auth_token
ronglian.app-id=your_app_id
ronglian.template-id=1

# ========================================
# 微信配置
# ========================================
wechat.app-id=your_wechat_app_id
wechat.app-secret=your_wechat_app_secret

# ========================================
# 阿里云 OSS 配置
# ========================================
aliyun.oss.endpoint=oss-cn-hangzhou.aliyuncs.com
aliyun.oss.access-key=your_access_key
aliyun.oss.access-secret=your_access_secret
aliyun.oss.bucket=your_bucket_name'

    # logback-spring.xml
    create_file "src/main/resources/logback-spring.xml" '<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 日志存放路径 -->
    <property name="log.path" value="logs"/>
    <!-- 日志输出格式 -->
    <property name="log.pattern" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{20} - [%method,%line] - %msg%n"/>

    <!-- 控制台输出 -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>

    <!-- 系统日志输出 -->
    <appender name="file_info" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/sys-info.log</file>
        <!-- 循环政策：基于时间创建日志文件 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志文件名格式 -->
            <fileNamePattern>${log.path}/sys-info.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 日志最大的历史 30 天 -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 过滤的级别 -->
            <level>INFO</level>
            <!-- 匹配时的操作：接收（记录） -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配时的操作：拒绝（不记录） -->
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="file_error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/sys-error.log</file>
        <!-- 循环政策：基于时间创建日志文件 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志文件名格式 -->
            <fileNamePattern>${log.path}/sys-error.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 日志最大的历史 30 天 -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 过滤的级别 -->
            <level>ERROR</level>
            <!-- 匹配时的操作：接收（记录） -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配时的操作：拒绝（不记录） -->
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- Spring日志级别控制 -->
    <logger name="org.springframework" level="warn"/>

    <!-- 系统模块日志级别控制 -->
    <logger name="top.mqxu" level="debug"/>

    <!-- 系统用户操作日志 -->
    <logger name="sys-user" level="debug"/>

    <!-- 系统日志输出级别 -->
    <root level="info">
        <appender-ref ref="console"/>
        <appender-ref ref="file_info"/>
        <appender-ref ref="file_error"/>
    </root>

    <!--开发环境:打印控制台日志-->
    <springProfile name="dev">
        <logger name="top.mqxu" level="debug"/>
    </springProfile>

    <!--生产环境:输出到文件-->
    <springProfile name="prod">
        <root level="info">
            <appender-ref ref="file_info"/>
            <appender-ref ref="file_error"/>
        </root>
    </springProfile>
</configuration>'

    # 5. 创建 SQL 脚本
    print_info "5. 创建数据库脚本..."
    create_file "sql/schema.sql" '-- 创建数据库
CREATE DATABASE IF NOT EXISTS api_template
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE api_template;

-- 用户表
CREATE TABLE `sys_user`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '\''主键'\'',
    `username`    varchar(50) NOT NULL COMMENT '\''用户名'\'',
    `password`    varchar(100) DEFAULT NULL COMMENT '\''密码'\'',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT '\''真实姓名'\'',
    `email`       varchar(100) DEFAULT NULL COMMENT '\''邮箱'\'',
    `mobile`      varchar(20)  DEFAULT NULL COMMENT '\''手机号'\'',
    `avatar`      varchar(200) DEFAULT NULL COMMENT '\''头像'\'',
    `nickname`    varchar(50)  DEFAULT NULL COMMENT '\''昵称'\'',
    `gender`      tinyint      DEFAULT '\''0'\'' COMMENT '\''性别 0:未知 1:男 2:女'\'',
    `wx_openid`   varchar(100) DEFAULT NULL COMMENT '\''微信openid'\'',
    `status`      tinyint      DEFAULT '\''1'\'' COMMENT '\''状态 0:禁用 1:正常'\'',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '\''创建时间'\'',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '\''更新时间'\'',
    `deleted`     tinyint      DEFAULT '\''0'\'' COMMENT '\''删除标记 0:未删除 1:已删除'\'',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`) USING BTREE,
    KEY           `idx_mobile` (`mobile`),
    KEY           `idx_wx_openid` (`wx_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='\''用户表'\'';

-- 默认密码: 123456
INSERT INTO `sys_user` (`username`,
                        `password`,
                        `nickname`,
                        `mobile`,
                        `status`)
VALUES ('\''admin'\'',
        '\'$2a$10$/ymWYgifpcgV.bRZYySYveiG0E7Q9h4A8bTpxm/9cEJd1aUZwIh26'\'',
        '\''管理员'\'',
        '\''13800138000'\'',
        1);'

    # 6. 创建 Docker 文件
    print_info "6. 创建 Docker 配置文件..."

    # Dockerfile
    create_file "Dockerfile" '# 使用多阶段构建
FROM maven:3.9.4-openjdk-17-slim AS builder

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jre-slim

# 安装必要的工具
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 创建应用目录
WORKDIR /app

# 从构建阶段复制 jar 文件
COPY --from=builder /app/target/api-template.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8083

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8083/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]'

    # docker-compose.yml
    create_file "docker-compose.yml" 'version: '\'3.8\''

services:
  # MySQL 服务
  mysql:
    image: mysql:8.0
    container_name: api-template-mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD:-123456}
      MYSQL_DATABASE: ${MYSQL_DATABASE:-api_template}
      MYSQL_USER: ${MYSQL_USER:-root}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD:-123456}
    ports:
      - "${MYSQL_PORT:-3306}:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/schema.sql:/docker-entrypoint-initdb.d/schema.sql:ro
    networks:
      - api-network
    command: >
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_0900_ai_ci
      --max_connections=1000
      --max_allowed_packet=128M

  # Redis 服务
  redis:
    image: redis:7-alpine
    container_name: api-template-redis
    restart: unless-stopped
    ports:
      - "${REDIS_PORT:-6379}:6379"
    volumes:
      - redis_data:/data
    networks:
      - api-network
    command: >
      redis-server
      --appendonly yes
      --maxmemory 256mb
      --maxmemory-policy allkeys-lru
    ${REDIS_PASSWORD:+--requirepass $REDIS_PASSWORD}

  # Spring Boot 应用服务
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: api-template-app
    restart: unless-stopped
    environment:
      # 数据库配置
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${MYSQL_DATABASE:-api_template}?useSSL=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: ${MYSQL_USER:-root}
      SPRING_DATASOURCE_PASSWORD: ${MYSQL_PASSWORD:-123456}

      # Redis 配置
      SPRING_DATA_REDIS_HOST: redis
      SPRING_DATA_REDIS_PORT: ${REDIS_PORT:-6379}
      SPRING_DATA_REDIS_PASSWORD: ${REDIS_PASSWORD:-}

      # 应用配置
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-prod}

      # JWT 配置
      JWT_SECRET: ${JWT_SECRET}
      JWT_EXPIRATION: ${JWT_EXPIRATION:-86400000}

      # 容联云短信配置（可选）
      RONGLIAN_SERVER: ${RONGLIAN_SERVER:-}
      RONGLIAN_PORT: ${RONGLIAN_PORT:-}
      RONGLIAN_ACCOUNT_SID: ${RONGLIAN_ACCOUNT_SID:-}
      RONGLIAN_AUTH_TOKEN: ${RONGLIAN_AUTH_TOKEN:-}
      RONGLIAN_APP_ID: ${RONGLIAN_APP_ID:-}
      RONGLIAN_TEMPLATE_ID: ${RONGLIAN_TEMPLATE_ID:-}

      # 微信配置（可选）
      WECHAT_APP_ID: ${WECHAT_APP_ID:-}
      WECHAT_APP_SECRET: ${WECHAT_APP_SECRET:-}

      # 阿里云 OSS 配置（可选）
      ALIYUN_OSS_ENDPOINT: ${ALIYUN_OSS_ENDPOINT:-}
      ALIYUN_OSS_ACCESS_KEY: ${ALIYUN_OSS_ACCESS_KEY:-}
      ALIYUN_OSS_ACCESS_SECRET: ${ALIYUN_OSS_ACCESS_SECRET:-}
      ALIYUN_OSS_BUCKET: ${ALIYUN_OSS_BUCKET:-}

      # JVM 配置
      JAVA_OPTS: ${JAVA_OPTS:--Xms512m -Xmx1024m -XX:+UseG1GC}
    ports:
      - "${APP_PORT:-8083}:8083"
    volumes:
      - app_logs:/app/logs
    networks:
      - api-network
    depends_on:
      - mysql
      - redis
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8083/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

# 数据卷定义
volumes:
  mysql_data:
    driver: local
  redis_data:
    driver: local
  app_logs:
    driver: local

# 网络定义
networks:
  api-network:
    driver: bridge'

    # .env.example
    create_file ".env.example" '# 项目配置
PROJECT_NAME=api-template

# 应用端口
APP_PORT=8083

# Spring 配置
SPRING_PROFILES_ACTIVE=prod

# JVM 配置
JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC

# JWT 配置（必须设置）
JWT_SECRET=your-256-bit-secret-key-for-jwt-token-generation-min-32-chars
JWT_EXPIRATION=86400000

# MySQL 配置
MYSQL_PORT=3306
MYSQL_DATABASE=api_template
MYSQL_ROOT_PASSWORD=123456
MYSQL_USER=root
MYSQL_PASSWORD=123456

# Redis 配置
REDIS_PORT=6379
REDIS_PASSWORD=

# 容联云短信配置（可选）
RONGLIAN_SERVER=app.cloopen.com
RONGLIAN_PORT=8883
RONGLIAN_ACCOUNT_SID=your_account_sid
RONGLIAN_AUTH_TOKEN=your_auth_token
RONGLIAN_APP_ID=your_app_id
RONGLIAN_TEMPLATE_ID=1

# 微信配置（可选）
WECHAT_APP_ID=your_wechat_app_id
WECHAT_APP_SECRET=your_wechat_app_secret

# 阿里云 OSS 配置（可选）
ALIYUN_OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
ALIYUN_OSS_ACCESS_KEY=your_access_key
ALIYUN_OSS_ACCESS_SECRET=your_access_secret
ALIYUN_OSS_BUCKET=your_bucket_name'

    # .gitignore
    create_file ".gitignore" '# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDE
.idea/
*.iws
*.iml
*.ipr
.vscode/
!.vscode/settings.json
!.vscode/tasks.json
!.vscode/launch.json
!.vscode/extensions.json

# OS
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Logs
logs/
*.log

# Environment
.env
.env.local
.env.development.local
.env.test.local
.env.production.local

# Application
application-secret.properties'

    # 由于脚本内容太长，我将在下一个回复中继续创建剩余的 Java 文件
    print_success "基础配置文件创建完成"
    print_info "由于脚本内容较长，剩余的 Java 文件将在后续脚本中创建..."
    echo

    print_info "已创建的文件："
    print_info "- pom.xml"
    print_info "- 启动类 Application.java"
    print_info "- 配置文件：application.yml, application-dev.yml, application-prod.yml"
    print_info "- 敏感配置示例：application-secret.properties.example"
    print_info "- 日志配置：logback-spring.xml"
    print_info "- 数据库脚本：sql/schema.sql"
    print_info "- Docker 配置：Dockerfile, docker-compose.yml"
    print_info "- 环境变量：.env.example"
    print_info "- Git 忽略：.gitignore"
    echo

    print_info "要继续创建所有 Java 类文件，请运行："
    print_info "bash scripts/create-java-files.sh"
}

# 执行主函数
main "$@"