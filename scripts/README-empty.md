# Spring Boot API 项目空文件创建脚本

本目录包含了创建 Spring Boot API 项目**空文件结构**的自动化脚本。

## 脚本说明

### 1. create-complete-project-empty.sh ⭐
**主脚本** - 一键创建完整项目的**空文件结构**
- 只创建空文件，不填充任何内容
- 包含友好的交互界面
- 显示项目信息和执行进度
- 提供详细的后续填充指导

```bash
bash scripts/create-complete-project-empty.sh
```

### 2. create-project-empty.sh
**基础脚本** - 创建项目空文件结构
- 创建所有必要的目录
- 创建所有配置文件（空）
- 创建所有 Java 类文件（空）
- 创建 Docker 和其他配置文件（空）

```bash
bash scripts/create-project-empty.sh
```

## 使用场景

### 何时使用空文件脚本？

1. **学习目的**: 想要手动编写代码，按照教程逐步填充内容
2. **教学演示**: 用于教学环境，让学生手动编写代码
3. **代码练习**: 需要空白项目进行代码练习
4. **从零开始**: 希望完全从头编写所有代码

### 与完整内容脚本的区别

| 功能 | 空文件脚本 | 完整内容脚本 |
|------|------------|--------------|
| 文件内容 | ❌ 空文件 | ✅ 完整代码 |
| 配置文件 | ❌ 空配置 | ✅ 完整配置 |
| 教程依赖 | ✅ 需要参考教程 | ✅ 可直接运行 |
| 学习价值 | ✅ 高 | ⭐ 中等 |
| 快速启动 | ❌ 需手动填充 | ✅ 一键启动 |

## 使用方法

### 方法一：一键创建空结构（推荐）

```bash
# 在项目根目录执行
bash scripts/create-complete-project-empty.sh
```

按照提示进行交互式操作，脚本会自动：
1. 创建完整的项目目录结构
2. 创建所有空配置文件
3. 创建所有空 Java 类文件
4. 提供详细的填充指导

### 方法二：直接创建空结构

```bash
# 直接执行基础脚本
bash scripts/create-project-empty.sh
```

## 创建的文件结构

脚本将创建以下空文件结构：

```
api-template/
├── src/main/java/top/mqxu/api/
│   ├── common/                    # 公共模块
│   │   ├── cache/                # 缓存工具（空文件）
│   │   │   ├── RedisKeys.java
│   │   │   └── RedisCache.java
│   │   ├── entity/               # 基础实体（空文件）
│   │   │   └── BaseEntity.java
│   │   ├── enums/                # 枚举类（空文件）
│   │   │   ├── DeletedEnum.java
│   │   │   ├── GenderEnum.java
│   │   │   └── StatusEnum.java
│   │   ├── exception/            # 异常处理（空文件）
│   │   │   ├── ServerException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── filter/               # 过滤器（空文件）
│   │   │   ├── LogFilter.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   ├── handler/              # 处理器（空文件）
│   │   │   └── SecurityExceptionHandler.java
│   │   ├── result/               # 统一响应（空文件）
│   │   │   ├── ResultCode.java
│   │   │   ├── Result.java
│   │   │   └── PageResult.java
│   │   ├── utils/                # 工具类（空文件）
│   │   │   ├── JwtUtils.java
│   │   │   └── SecurityUtils.java
│   │   └── model/                # 模型（空文件）
│   │       └── CustomUserDetails.java
│   ├── config/                   # 配置类（空文件）
│   │   ├── MyBatisPlusConfig.java
│   │   ├── RedisConfig.java
│   │   ├── JacksonConfig.java
│   │   ├── Knife4jConfig.java
│   │   └── SecurityConfig.java
│   ├── infrastructure/           # 基础设施层（空文件）
│   │   ├── oss/                  # 对象存储（空文件）
│   │   │   ├── AliyunOssProperties.java
│   │   │   ├── OssService.java
│   │   │   └── impl/AliyunOssServiceImpl.java
│   │   ├── sms/                  # 短信服务（空文件）
│   │   │   ├── RongLianSmsProperties.java
│   │   │   ├── SmsProvider.java
│   │   │   └── impl/RongLianSmsServiceImpl.java
│   │   └── wechat/               # 微信服务（空文件）
│   │       ├── WechatProperties.java
│   │       ├── WechatUserInfo.java
│   │       ├── WechatService.java
│   │       └── impl/WechatServiceImpl.java
│   ├── module/                   # 业务模块
│   │   ├── auth/                 # 认证模块（空文件）
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   └── impl/AuthServiceImpl.java
│   │   │   └── model/
│   │   │       ├── dto/
│   │   │       │   ├── LoginDTO.java
│   │   │       │   ├── SmsLoginDTO.java
│   │   │       │   ├── WechatLoginDTO.java
│   │   │       │   ├── BindMobileDTO.java
│   │   │       │   └── ChangeMobileDTO.java
│   │   │       └── vo/
│   │   │           └── TokenVO.java
│   │   └── user/                 # 用户模块（空文件）
│   │       ├── controller/
│   │       │   └── UserController.java
│   │       ├── service/
│   │       │   ├── UserService.java
│   │       │   └── impl/UserServiceImpl.java
│   │       ├── mapper/
│   │       │   └── UserMapper.java
│   │       ├── convert/
│   │       │   └── UserConvert.java
│   │       └── model/
│   │           ├── entity/
│   │           │   └── UserEntity.java
│   │           ├── dto/
│   │           │   └── UserDTO.java
│   │           └── vo/
│   │               └── UserVO.java
│   └── Application.java          # 启动类（空文件）
├── src/main/resources/           # 配置文件（空文件）
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   ├── application-secret.properties.example
│   └── logback-spring.xml
├── sql/                         # 数据库脚本（空文件）
│   └── schema.sql
├── scripts/                      # 脚本文件
├── docs/                         # 文档目录
├── Dockerfile                    # Docker 镜像（空文件）
├── docker-compose.yml            # Docker 编排（空文件）
├── .env.example                  # 环境变量模板（空文件）
├── .gitignore                    # Git 忽略文件（空文件）
├── README.md                     # 项目说明（空文件）
└── pom.xml                       # Maven 配置（空文件）
```

## 后续填充步骤

项目空结构创建完成后，请按照 `docs/从零搭建教程.md` 中的详细步骤填充文件内容：

### 1. 填充配置文件

按照教程中的配置文件章节填充：
- `pom.xml` - Maven 依赖配置
- `src/main/resources/application.yml` - 主配置文件
- `src/main/resources/application-dev.yml` - 开发环境配置
- `src/main/resources/application-prod.yml` - 生产环境配置
- `src/main/resources/application-secret.properties` - 敏感配置
- `src/main/resources/logback-spring.xml` - 日志配置

### 2. 填充数据库脚本

按照教程中的数据库设计章节填充：
- `sql/schema.sql` - 数据库表结构和初始数据

### 3. 填充 Java 类文件

按照教程中的顺序填充 Java 类：

#### 3.1 公共模块 (common)
- `BaseEntity.java` - 基础实体类
- 枚举类：`DeletedEnum.java`, `GenderEnum.java`, `StatusEnum.java`
- 响应类：`ResultCode.java`, `Result.java`, `PageResult.java`
- 异常类：`ServerException.java`, `GlobalExceptionHandler.java`
- 缓存类：`RedisKeys.java`, `RedisCache.java`
- 工具类：`JwtUtils.java`, `SecurityUtils.java`
- 过滤器：`LogFilter.java`, `JwtAuthenticationFilter.java`
- 处理器：`SecurityExceptionHandler.java`

#### 3.2 配置类 (config)
- `MyBatisPlusConfig.java` - MyBatis-Plus 配置
- `RedisConfig.java` - Redis 配置
- `JacksonConfig.java` - JSON 序列化配置
- `Knife4jConfig.java` - API 文档配置
- `SecurityConfig.java` - Spring Security 配置

#### 3.3 基础设施层 (infrastructure)
- 短信服务：`RongLianSmsProperties.java`, `SmsProvider.java`, `RongLianSmsServiceImpl.java`
- 对象存储：`AliyunOssProperties.java`, `OssService.java`, `AliyunOssServiceImpl.java`
- 微信服务：`WechatProperties.java`, `WechatService.java`, `WechatServiceImpl.java`

#### 3.4 认证模块 (auth)
- DTO：`LoginDTO.java`, `SmsLoginDTO.java`, `WechatLoginDTO.java` 等
- VO：`TokenVO.java`
- Service：`AuthService.java`, `AuthServiceImpl.java`
- Controller：`AuthController.java`

#### 3.5 用户模块 (user)
- Entity：`UserEntity.java`
- DTO：`UserDTO.java`
- VO：`UserVO.java`
- Mapper：`UserMapper.java`
- Convert：`UserConvert.java`
- Service：`UserService.java`, `UserServiceImpl.java`
- Controller：`UserController.java`

#### 3.6 启动类
- `Application.java` - Spring Boot 启动类

### 4. 填充 Docker 配置

按照教程中的 Docker 部署章节填充：
- `Dockerfile` - Docker 镜像构建文件
- `docker-compose.yml` - Docker 编排文件
- `.env.example` - 环境变量模板
- `.gitignore` - Git 忽略文件

### 5. 填充项目文档

- `README.md` - 项目说明文档

## 填充完成后的步骤

所有文件填充完成后：

1. **配置敏感信息**：
   ```bash
   cp src/main/resources/application-secret.properties.example src/main/resources/application-secret.properties
   # 编辑文件，填入实际配置
   ```

2. **创建数据库**：
   ```bash
   mysql -u root -p
   source sql/schema.sql
   ```

3. **启动项目**：
   ```bash
   mvn spring-boot:run
   ```

4. **访问应用**：
   - API 文档: http://localhost:8083/doc.html
   - 健康检查: http://localhost:8083/actuator/health

## 教程依赖

使用空文件脚本**必须参考**以下教程文档：
- **主要教程**: `docs/从零搭建教程.md` - 完整的从零搭建教程
- **配置参考**: 现有项目中的文件内容

## 注意事项

1. **必须参考教程**: 空文件脚本不提供任何代码内容，必须参考教程文档手动填充
2. **文件顺序**: 建议按照教程中的顺序填充文件，确保依赖关系正确
3. **配置重要性**: 配置文件的填充至关重要，直接影响项目能否正常启动
4. **测试验证**: 每完成一个模块的填充，建议进行测试验证

## 脚本特性

- ✅ **一键创建**: 完整的项目空文件结构
- ✅ **学习友好**: 适合学习和教学场景
- ✅ **逐步填充**: 支持按照教程逐步填充内容
- ✅ **完整结构**: 包含所有必要的文件和目录
- ✅ **交互式界面**: 友好的用户交互
- ✅ **重复执行**: 支持重复执行，已存在文件会跳过

## 与完整内容脚本的对比

| 特性 | 空文件脚本 | 完整内容脚本 |
|------|------------|--------------|
| 学习体验 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 代码质量 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 启动速度 | ❌ 需要填充 | ✅ 一键启动 |
| 教学价值 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 适用场景 | 学习、教学 | 开发、演示 |

选择适合你需求的脚本版本！

---

**脚本版本**: 1.0.0
**最后更新**: 2024年10月20日
**兼容性**: Spring Boot 3.5.5 + JDK 17+