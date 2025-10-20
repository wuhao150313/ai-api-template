# Spring Boot API 项目创建脚本

本目录包含了创建完整 Spring Boot API 项目的自动化脚本。

## 脚本说明

### 1. create-complete-project.sh
**主脚本** - 一键创建完整项目
- 推荐使用此脚本，它会自动执行所有步骤
- 包含友好的交互界面
- 显示项目信息和执行进度
- 提供后续操作指导

```bash
bash scripts/create-complete-project.sh
```

### 2. create-project.sh
**步骤 1** - 创建基础项目结构
- 创建项目目录结构
- 生成 pom.xml
- 创建配置文件（application.yml 等）
- 生成数据库脚本
- 创建 Docker 配置文件

```bash
bash scripts/create-project.sh
```

### 3. create-java-files.sh
**步骤 2** - 创建 Java 类文件
- 创建公共模块（common）
- 创建配置类（config）
- 生成基础工具类和异常处理

```bash
bash scripts/create-java-files.sh
```

### 4. create-business-files.sh
**步骤 3** - 创建业务模块文件
- 创建基础设施层（infrastructure）
- 创建认证模块（auth）
- 创建用户模块（user）

```bash
bash scripts/create-business-files.sh
```

## 使用方法

### 方法一：一键创建（推荐）

```bash
# 在项目根目录执行
bash scripts/create-complete-project.sh
```

按照提示进行交互式操作，脚本会自动：
1. 创建完整的项目结构
2. 生成所有配置文件
3. 创建所有 Java 类
4. 提供后续操作指导

### 方法二：分步创建

如果需要分步创建或调试，可以按顺序执行：

```bash
# 步骤 1：创建基础结构
bash scripts/create-project.sh

# 步骤 2：创建 Java 类
bash scripts/create-java-files.sh

# 步骤 3：创建业务模块
bash scripts/create-business-files.sh
```

## 项目结构

脚本将创建以下项目结构：

```
api-template/
├── src/main/java/top/mqxu/api/
│   ├── common/                    # 公共模块
│   │   ├── cache/                # 缓存工具
│   │   ├── entity/               # 基础实体
│   │   ├── enums/                # 枚举类
│   │   ├── exception/            # 异常处理
│   │   ├── filter/               # 过滤器
│   │   ├── handler/              # 处理器
│   │   ├── result/               # 统一响应
│   │   └── utils/                # 工具类
│   ├── config/                   # 配置类
│   │   ├── MyBatisPlusConfig.java
│   │   ├── RedisConfig.java
│   │   ├── JacksonConfig.java
│   │   ├── Knife4jConfig.java
│   │   └── SecurityConfig.java
│   ├── infrastructure/           # 基础设施层
│   │   ├── oss/                  # 对象存储
│   │   ├── sms/                  # 短信服务
│   │   └── wechat/               # 微信服务
│   ├── module/                   # 业务模块
│   │   ├── auth/                 # 认证模块
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   └── model/
│   │   └── user/                 # 用户模块
│   │       ├── controller/
│   │       ├── service/
│   │       ├── mapper/
│   │       └── model/
│   └── Application.java          # 启动类
├── src/main/resources/           # 配置文件
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   ├── application-secret.properties.example
│   └── logback-spring.xml
├── sql/                         # 数据库脚本
│   └── schema.sql
├── scripts/                      # 脚本文件
├── docs/                         # 文档
├── Dockerfile                    # Docker 镜像
├── docker-compose.yml            # Docker 编排
├── .env.example                  # 环境变量模板
├── .gitignore                    # Git 忽略文件
└── pom.xml                       # Maven 配置
```

## 技术栈

- **框架**: Spring Boot 3.5.5
- **安全**: Spring Security + JWT
- **数据库**: MySQL 8.0 + MyBatis-Plus
- **缓存**: Redis
- **文档**: Knife4j (Swagger 3)
- **工具**: Hutool、Lombok、MapStruct
- **部署**: Docker + Docker Compose

## 后续步骤

项目创建完成后，请按照以下步骤完成搭建：

### 1. 配置敏感信息

```bash
cp src/main/resources/application-secret.properties.example src/main/resources/application-secret.properties
nano src/main/resources/application-secret.properties
```

**重要配置项：**
- `jwt.secret`: JWT 密钥（必须设置，至少 32 位字符）
- `ronglian.*`: 容联云短信配置（可选）
- `wechat.*`: 微信配置（可选）
- `aliyun.oss.*`: 阿里云 OSS 配置（可选）

### 2. 创建数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行数据库脚本
source sql/schema.sql
```

### 3. 启动项目

```bash
# 开发环境启动
mvn spring-boot:run

# 或者打包后启动
mvn clean package -DskipTests
java -jar target/api-template.jar
```

### 4. 访问应用

- **API 文档**: http://localhost:8083/doc.html
- **健康检查**: http://localhost:8083/actuator/health

### 5. Docker 部署（可选）

```bash
# 复制环境变量文件
cp .env.example .env
nano .env  # 编辑环境变量

# 启动 Docker 服务
docker-compose up -d
```

## 默认账号

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 管理员账号 |

## 接口测试

### 账号密码登录

```bash
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

### 获取用户信息

```bash
# 先获取 Token
TOKEN=$(curl -s -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }' | jq -r '.data.token')

# 使用 Token 获取用户信息
curl -X GET http://localhost:8083/api/user/user-info \
  -H "Authorization: Bearer $TOKEN"
```

## 常见问题

### 1. 脚本执行权限问题

```bash
chmod +x scripts/*.sh
```

### 2. Java 版本问题

确保使用 JDK 17 或更高版本：

```bash
java -version
```

### 3. Maven 构建问题

确保 Maven 版本 3.6+：

```bash
mvn -version
```

### 4. 数据库连接问题

- 检查 MySQL 服务是否启动
- 检查数据库连接配置
- 确认数据库用户权限

### 5. Redis 连接问题

- 检查 Redis 服务是否启动
- 检查 Redis 连接配置

## 注意事项

1. **敏感信息安全**: `application-secret.properties` 文件包含敏感信息，请勿提交到代码仓库
2. **JWT 密钥**: 必须设置一个强密码作为 JWT 密钥，至少 32 位字符
3. **端口占用**: 默认端口为 8083，如需修改请更新配置文件
4. **数据库字符集**: 确保数据库使用 utf8mb4 字符集
5. **Docker 环境**: 使用 Docker 部署时，确保已安装 Docker 和 Docker Compose

## 脚本特性

- ✅ **一键创建**: 完整的项目结构和代码
- ✅ **交互式界面**: 友好的用户交互
- ✅ **进度显示**: 清晰的执行进度
- ✅ **错误处理**: 完善的错误提示
- ✅ **重复执行**: 支持重复执行，已存在文件会跳过
- ✅ **分步执行**: 支持分步创建和调试
- ✅ **完整文档**: 详细的使用说明和配置指导

## 支持

如有问题或建议，请：
1. 查看项目文档 `docs/从零搭建教程.md`
2. 检查脚本输出的错误信息
3. 确认环境配置是否正确

---

**脚本版本**: 1.0.0
**最后更新**: 2024年10月20日
**兼容性**: Spring Boot 3.5.5 + JDK 17+