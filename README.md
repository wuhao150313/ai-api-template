# Spring Boot API 项目脚手架

🚀 基于 Spring Boot 3.5.5 的企业级 API 项目脚手架，提供完整的基础设施和最佳实践。

## 📋 特性

- ✅ Spring Boot 3.5.5
- ✅ MyBatis-Plus 3.5.7
- ✅ JWT 认证
- ✅ Redis 缓存
- ✅ HikariCP 连接池
- ✅ Knife4j API 文档
- ✅ Docker 支持
- ✅ 统一响应格式
- ✅ 全局异常处理
- ✅ 参数校验
- ✅ 逻辑删除
- ✅ 自动填充
- ✅ 短信验证码登录
- ✅ 微信登录支持


## 📝 项目结构

```
api-template/
├── src/
│   ├── main/
│   │   ├── top/mqxu/api/
│   │   │   ├── common/          # 公共模块
│   │   │   │   ├── cache/       # 缓存封装
│   │   │   │   ├── config/      # 配置类
│   │   │   │   ├── exception/   # 异常类
│   │   │   │   ├── filter/      # 过滤器
│   │   │   │   ├── handler/     # 处理器
│   │   │   │   ├── result/      # 统一响应
│   │   │   │   └── utils/       # 工具类
│   │   │   ├── config/          # Spring 配置
│   │   │   │   ├── JacksonConfig
│   │   │   │   ├── MybatisPlusConfig
│   │   │   │   ├── RedisConfig
│   │   │   │   ├── SecurityConfig
│   │   │   │   └── Knife4jConfig
│   │   │   ├── infrastructure/   # 基础设施层
│   │   │   │   ├── oss/         # 对象存储
│   │   │   │   ├── sms/         # 短信服务
│   │   │   │   └── wechat/      # 微信服务
│   │   │   ├── module/          # 业务模块
│   │   │   │   ├── auth/        # 认证模块
│   │   │   │   └── user/        # 用户模块
│   │   │   └── Application.java  # 启动类
│   │   └── resources/
│   │       ├── application.yml   # 主配置
│   │       ├── application-dev.yml # 开发环境配置
│   │       ├── application-prod.yml # 生产环境配置
│   │       └── mapper/          # MyBatis XML
│   └── test/                    # 测试代码
├── sql/
│   └── schema.sql               # 数据库脚本
├── docker-compose.yml           # Docker 编排
├── Dockerfile                   # Docker 镜像
├── .env.example                 # 环境变量模板
└── README.md                    # 项目文档
```

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/your-username/spring-boot-api-template.git
```

### 2. 配置数据库

执行 sql/schema.sql


### 3. 配置应用

#### 认证机制说明

本项目使用 Spring Security + JWT 进行认证：
- **无需认证的接口**：登录相关接口、API 文档、健康检查等
- **需要认证的接口**：除上述接口外的所有接口（包括用户管理、登出等）
- **认证方式**：在请求头中添加 `Authorization: Bearer <token>`

#### 本地开发配置

```bash
# 复制敏感配置文件
cp src/main/resources/application-secret.properties.example application-secret.properties

# 编辑配置文件
nano application-secret.properties  # 配置 JWT 密钥等敏感信息
```

#### Docker 部署配置

```bash
# 复制环境变量文件
cp .env.example .env

# 编辑环境变量文件
nano .env
```


#### 配置文件说明

1. **application.yml**：基础配置（框架、业务配置等）
2. **application-dev.yml**：开发环境特定配置（数据库、连接池、日志等）
3. **application-secret.properties**：敏感配置（JWT 密钥、API Key 等）

#### 配置优先级说明

1. **命令行参数** (最高优先级)
2. **环境变量**
3. **外部配置文件** (`application-secret.properties`)
4. **打包的配置文件** (`application.yml`, `application-dev.yml`)

**推荐**：本地开发使用外部配置文件，生产环境使用环境变量

### 4. 启动项目

#### 方式一：本地开发

```bash
# 启动项目
mvn spring-boot:run

# 或使用 Maven Wrapper
./mvnw spring-boot:run
```

#### 方式二：Docker 部署

```bash
# 使用 docker-compose 启动所有服务（MySQL + Redis + 应用）
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止服务
docker-compose down
```

### 5. 访问应用

- **应用接口**: http://localhost:8080
- **API 文档**: http://localhost:8080/doc.html
- **健康检查**: http://localhost:8080/actuator/health

## 📚 API 接口说明

### 认证接口

- **账号密码登录**: `POST /api/auth/login`
  ```json
  {
    "username": "admin",
    "password": "123456"
  }
  ```

- **发送验证码**: `POST /api/auth/send-sms-code`
  ```json
  {
    "mobile": "13800138000"
  }
  ```

- **短信登录**: `POST /api/auth/sms-login`
  ```json
  {
    "mobile": "13800138000",
    "code": "123456"
  }
  ```

- **微信登录**: `POST /api/auth/wechat-login`
  ```json
  {
    "code": "wx_code"
  }
  ```

- **退出登录**: `POST /api/auth/logout`
- **绑定手机号**: `POST /api/auth/bind-mobile`
- **换绑手机号**: `POST /api/auth/change-mobile`

### 用户管理接口

- **用户分页列表**: `GET /api/user/page?page=1&size=10`
- **根据 ID 查询用户**: `GET /api/user/{id}`
- **获取当前用户信息**: `GET /api/user/user-info`
- **用户注册**: `POST /api/user/register`
- **更新个人信息**: `PUT /api/user/update-profile`

### 认证说明

- **无需认证**：登录相关接口、API 文档、健康检查等
- **需要认证**：除上述接口外的所有接口（需要携带 JWT Token）
- **认证方式**：在请求头中添加 `Authorization: Bearer <token>`

## 🔧 开发配置

### IDE 配置

推荐使用 IntelliJ IDEA：

1. 导入项目为 Maven 项目
2. 设置 JDK 17+
3. 启用 Lombok 插件
4. 设置代码格式化为统一风格

### 环境配置

项目支持多环境配置：

- **开发环境**: `application-dev.yml`
- **生产环境**: `application-prod.yml`
- **主配置**: `application.yml`

切换环境：
```bash
# 开发环境（默认）
mvn spring-boot:run -Dspring.profiles.active=dev

# 生产环境
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 🛠️ 技术栈

- **Spring Boot**: 3.5.5
- **MyBatis-Plus**: 3.5.7
- **JWT**: 0.12.6
- **Redis**: Spring Boot Starter
- **Druid**: 1.2.25
- **Hutool**: 5.8.34
- **Knife4j**: 4.5.0



## 🎯 开发规范

### Git 提交规范

```bash
[新增] 新功能
[修复] Bug 修复
[优化] 代码优化
[重构] 代码重构
[文档] 文档更新
[配置] 配置修改
```

### 代码规范

- 使用 `@Data`、`@RequiredArgsConstructor` 简化代码
- 统一响应格式 `Result<T>`
- 统一异常处理 `ServerException`
- RESTful 风格接口设计
- 参数校验使用 `@Validated`、`@NotBlank` 等注解


## 🐳 Docker 部署

### 1. 环境配置

复制环境变量文件并根据需要修改：

```bash
# 复制环境变量文件
cp .env.example .env

# 编辑环境变量文件
nano .env
```

**主要环境变量说明：**

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `PROJECT_NAME` | api-template | 项目名称，用于容器命名 |
| `DATABASE_NAME` | api_template | 数据库名称 |
| `MYSQL_ROOT_PASSWORD` | 123456 | MySQL root 密码 |
| `REDIS_PASSWORD` | (空) | Redis 密码 |
| `APP_PORT` | 8080 | 应用端口 |
| `JWT_SECRET` | - | JWT 密钥（必须设置） |
| `SPRING_PROFILES_ACTIVE` | prod | Spring 配置环境 |

**可选配置（按需配置）：**
- **容联云短信**: `RONGLIAN_*` 系列变量
- **阿里云 OSS**: `ALIYUN_OSS_*` 系列变量
- **微信 API**: `WECHAT_*` 系列变量

### 2. 使用 Docker Compose 部署

```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看应用日志
docker-compose logs -f app

# 查看所有服务日志
docker-compose logs -f

# 停止并删除容器
docker-compose down

# 停止容器但保留数据卷
docker-compose stop

# 重新启动服务
docker-compose restart
```

### 3. 服务组成

**MySQL 数据库服务**
- 版本：MySQL 8.0
- 端口：`${MYSQL_PORT:-3306}`
- 数据持久化：`mysql_data` 数据卷
- 自动化数据库初始化：`sql/schema.sql`

**Redis 缓存服务**
- 版本：Redis 7-alpine
- 端口：`${REDIS_PORT:-6379}`
- 数据持久化：`redis_data` 数据卷
- 支持密码认证

**Spring Boot 应用服务**
- 端口：`${APP_PORT:-8080}`
- JVM 内存：`${JAVA_OPTS:--Xms512m -Xmx1024m -XX:+UseG1GC}`
- 健康检查：`/actuator/health`
- 依赖数据库和 Redis 服务启动

### 4. 数据卷管理

```bash
# 查看数据卷
docker volume ls

# 备份 MySQL 数据
docker run --rm -v api-template_mysql_data:/data -v $(pwd):/backup alpine tar czf /backup/mysql-backup.tar.gz -C /data .

# 备份 Redis 数据
docker run --rm -v api-template_redis_data:/data -v $(pwd):/backup alpine tar czf /backup/redis-backup.tar.gz -C /data .

# 恢复数据
docker run --rm -v api-template_mysql_data:/data -v $(pwd):/backup alpine tar xzf /backup/mysql-backup.tar.gz -C /data
```

### 5. 网络配置

所有服务都在自定义网络 `api-network` 中运行，确保：
- 服务间可以通过容器名互相访问
- 与主机网络隔离
- 提高安全性

### 6. 生产环境建议

```bash
# 1. 使用强密码
MYSQL_ROOT_PASSWORD=your-strong-password
REDIS_PASSWORD=your-redis-password

# 2. 配置合适的 JVM 内存
JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC

# 3. 设置 JWT 密钥
JWT_SECRET=your-256-bit-secret-key-for-jwt-token-generation-min-32-chars

# 4. 限制容器资源（在 docker-compose.yml 中添加）
services:
  app:
    deploy:
      resources:
        limits:
          memory: 2G
          cpus: '1.0'
        reservations:
          memory: 512M
          cpus: '0.5'
```

### 7. 故障排除

```bash
# 查看容器状态
docker-compose ps

# 检查容器资源使用
docker stats

# 进入容器调试
docker-compose exec app sh
docker-compose exec mysql mysql -u root -p
docker-compose exec redis redis-cli

# 重新构建镜像
docker-compose build --no-cache

# 清理未使用的资源
docker system prune -f
```
