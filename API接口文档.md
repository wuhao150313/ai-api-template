## 概述

本项目后端提供了一组围绕「校园智能助手 / 用户与认证 / 会话管理 / AI 问答与推荐」的 RESTful API。  
本文档面向前端开发者和 AI 代理，**列出当前项目中未被注释的所有对外接口**，包括：

- **请求方法 / URL / 说明**
- **请求头要求（特别是鉴权）**
- **请求体 / 查询参数结构**
- **统一响应结构**

如无特殊说明：

- 所有未标注鉴权的接口默认 **无需登录**。
- 标注了 `Authorization` 的接口，需在请求头携带 `Authorization: Bearer <token>`。

---

## 统一响应结构（Result<T>）

除个别返回原始字符串或 Flux 流的接口，其余大部分业务接口统一返回 `Result<T>`：

```text
{
  "code": number,    // 业务状态码，200 表示成功，其他为失败
  "message": string, // 提示信息
  "data": T | null   // 业务数据
}
```

其中 `code` 与 `message` 由后端的 `ResultCode` 枚举维护（例如：成功、失败、未授权等）。

---

## 鉴权与 Token

### 登录返回的 Token 结构（TokenVO）

```text
{
  "token": "string",   // Bearer token 字符串
  "expiresIn": number  // 过期时间（单位：秒）
}
```

前端在需要鉴权的接口中，通过请求头携带：

```http
Authorization: Bearer <token>
```

---

## 一、简单 Agent 对话接口（无记忆）

### 1.1 GET /api/v1/chat

- **说明**：调用简单 Agent，与 AI 进行一次性问答，对话无多轮记忆。
- **控制器**：`SimpleAgentController`
- **鉴权**：无需

**请求**

- **方法**：`GET`
- **URL**：`/api/v1/chat`
- **查询参数**：
  - `question` (string, 可选，默认值：`图书馆在哪里`)

```http
GET /api/v1/chat?question=图书馆在哪里
```

**响应**

- **内容类型**：`text/plain; charset=utf-8`
- **示例**：

```text
这里是图书馆的位置说明……
```

---

## 二、高级 Agent 对话接口（带记忆）

### 2.1 POST /api/v2/chat

- **说明**：与 Agent 进行对话，后端会为每个 `userId` 维护一个 threadId，从而实现带记忆的多轮对话。
- **控制器**：`AdvancedAgentController`
- **鉴权**：无需（通过 `userId` 识别会话）

**请求**

- **方法**：`POST`
- **URL**：`/api/v2/chat`
- **请求体 RequestDTO**：

```json
{
  "userId": "string",   // 用户唯一标识（前端自行维护，如登录用户ID或游客ID）
  "message": "string"   // 本轮用户输入内容
}
```

**响应体 AssistantResponse**

```json
{
  "answer": "string",           // AI 返回的自然语言回答
  "type": "string",             // 回答类型（示例：general）
  "suggestion": "string",       // 给用户的建议文案
  "needsFurtherHelp": false     // 是否需要进一步帮助
}
```

> 实际字段取决于 `AssistantResponse` 类定义，当前实现中：
> - `type` 固定为 `"general"`
> - `suggestion` 固定为 `"请根据你的需求，给出一个建议"`
> - `needsFurtherHelp` 固定为 `false`

---

### 2.2 GET /api/v2/history/{userId}

- **说明**：根据 `userId` 查询该用户对应的对话 threadId 和简化的历史信息（目前为占位实现，实际历史内容尚未接入）。
- **控制器**：`AdvancedAgentController`
- **鉴权**：无需

**请求**

- **方法**：`GET`
- **URL**：`/api/v2/history/{userId}`
- **路径参数**：
  - `userId` (string, 必填)：用户 ID

**响应**

- **成功示例**：

```json
{
  "userId": "u1",
  "threadId": "uuid-string",
  "history": "历史记录功能需要进一步实现"
}
```

- **未找到用户历史时**：

```json
{
  "error": "未找到该用户的历史记录"
}
```

---

## 三、AI 问答 / 联网搜索 / 图片理解接口

> 控制器：`AIQnaController`  
> 基础路径：`/ai/qna`  
> 鉴权：均无需

### 3.1 GET /ai/qna/ask

- **说明**：普通 AI 问答，直接返回完整回答字符串。

**请求**

- **方法**：`GET`
- **URL**：`/ai/qna/ask`
- **查询参数**：
  - `question` (string, 可选，默认：`你是谁`)

**响应**

- **内容类型**：`text/plain; charset=utf-8`

```text
我是一个基于大模型的校园智能助手……
```

---

### 3.2 GET /ai/qna/ask/stream

- **说明**：流式 AI 问答接口，基于 `Flux<String>` 分段返回内容，适合前端流式显示。

**请求**

- **方法**：`GET`
- **URL**：`/ai/qna/ask/stream`
- **查询参数**：
  - `question` (string, 可选，默认：`你是谁`)

**响应**

- **返回类型**：`text/event-stream` / `application/stream+json`（取决于配置）  
  前端需按流式消费多段字符串片段，拼接后为完整回答。

---

### 3.3 GET /ai/qna/web-search

- **说明**：带联网搜索能力的问答，返回流式结果。

**请求**

- **方法**：`GET`
- **URL**：`/ai/qna/web-search`
- **查询参数**：
  - `question` (string, 可选，默认：`你是谁`)

**响应**

- 返回 `Flux<String>`，同样为流式字符串片段。

---

### 3.4 GET /ai/qna/switch-model

- **说明**：切换后端所使用的大模型。

**请求**

- **方法**：`GET`
- **URL**：`/ai/qna/switch-model`
- **查询参数**：
  - `model` (string, 可选，默认：`qwen-turbo`)

**响应**

- **内容类型**：`text/plain; charset=utf-8`
- **示例**：

```text
切换模型成功
```

---

### 3.5 GET /ai/qna/image/analyze/url

- **说明**：通过图片 URL 进行多模态图片分析，结果以流式文本返回。

**请求**

- **方法**：`GET`
- **URL**：`/ai/qna/image/analyze/url`
- **查询参数**：
  - `prompt` (string, 可选，默认：`请分析图片内容`)
  - `imageUrl` (string, 可选，默认为示例 OSS 图片地址)

**响应**

- 返回 `Flux<String>`，流式输出图片分析结果。

---

## 四、AI 模型管理接口

> 控制器：`AIModelController`  
> 基础路径：`/api/ai/model`  
> 鉴权：**全部需要 `Authorization` 头**

### 4.1 GET /api/ai/model/list

- **说明**：获取当前系统中可用的模型列表。

**请求**

- **方法**：`GET`
- **URL**：`/api/ai/model/list`
- **请求头**：
  - `Authorization: Bearer <token>`

**响应**

- **类型**：`Result<List<AIModelVO>>`
- `AIModelVO` 典型字段（根据 VO 类，可能包含）：

```json
{
  "name": "qwen-turbo",
  "label": "通义千问 Turbo",
  "description": "适合通用对话场景的模型",
  "...": "其他模型相关信息"
}
```

---

### 4.2 GET /api/ai/model/current

- **说明**：获取当前正在使用的模型信息。

**请求**

- **方法**：`GET`
- **URL**：`/api/ai/model/current`
- **请求头**：
  - `Authorization: Bearer <token>`

**响应**

- **类型**：`Result<AIModelVO>`

---

### 4.3 POST /api/ai/model/switch

- **说明**：切换当前模型。

**请求**

- **方法**：`POST`
- **URL**：`/api/ai/model/switch`
- **请求头**：
  - `Authorization: Bearer <token>`
- **请求参数（表单或 query）**：
  - `modelName` (string, 必填)

> 注意：接口使用 `@RequestParam`，因此默认接收 form-url-encoded 或 query 形式。

**示例请求**

```http
POST /api/ai/model/switch?modelName=qwen-turbo
Authorization: Bearer <token>
```

**响应**

- **类型**：`Result<AIModelVO>`（切换后的模型信息）

---

## 五、课程推荐接口

> 控制器：`CourseRecommendController`  
> 基础路径：`/ai/recommend`  
> 鉴权：无需

### 5.1 POST /ai/recommend/courses

- **说明**：根据用户水平与已完成课程，返回推荐课程列表。

**请求**

- **方法**：`POST`
- **URL**：`/ai/recommend/courses`
- **请求体 RecommendRequest**：

```json
{
  "userLevel": "beginner | intermediate | advanced", // 用户当前水平，自定义枚举
  "completedCourses": [ "Java 入门", "数据结构基础" ]  // 已完成课程名称列表
}
```

**响应**

- **类型**：`Result<List<string>>`
- **示例**：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    "JavaWeb 开发实战",
    "Spring Boot 入门到实战"
  ]
}
```

---

## 六、作业点评接口

> 控制器：`HomeworkReviewController`  
> 基础路径：`/ai/homework`  
> 鉴权：无需

### 6.1 POST /ai/homework/review

- **说明**：提交作业题目与学生答案，AI 返回点评结果。

**请求**

- **方法**：`POST`
- **URL**：`/ai/homework/review`
- **请求体 ReviewRequest**：

```json
{
  "taskDescription": "请实现一个快速排序算法……",
  "studentAnswer": "public void quickSort(int[] arr) { ... }"
}
```

**响应**

- **类型**：`Result<ReviewResult>`
- `ReviewResult` 典型字段（根据类，可包含如下信息）：

```json
{
  "score": 90,
  "comment": "整体实现正确，时间复杂度达标，但边界条件处理略有瑕疵。",
  "suggestions": [
    "建议补充对空数组的判断",
    "增加对重复元素较多情形的测试用例"
  ]
}
```

- 出错时：

```json
{
  "code": 500,
  "message": "AI 调用失败：<错误信息>",
  "data": null
}
```

---

## 七、知识总结 / 思维导图接口

> 控制器：`KnowledgeSummaryController`  
> 基础路径：`/ai/summary`  
> 鉴权：无需

### 7.1 POST /ai/summary/course

- **说明**：对课程内容进行知识点总结。

**请求**

- **方法**：`POST`
- **URL**：`/ai/summary/course`
- **请求体 SummaryRequest**：

```json
{
  "courseContent": "本课程主要讲解 Java 基础语法、面向对象、集合框架……"
}
```

**响应**

- **类型**：`Result<string>`
- **示例**：

```json
{
  "code": 200,
  "message": "success",
  "data": "1. Java 基础语法；2. 面向对象三大特性；3. 集合框架应用场景……"
}
```

---

### 7.2 POST /ai/summary/mindmap

- **说明**：基于课程内容生成思维导图描述（通常以某种可视化结构/标记语言返回）。

**请求**

- **方法**：`POST`
- **URL**：`/ai/summary/mindmap`
- **请求体 SummaryRequest**：

```json
{
  "courseContent": "本课程主要讲解 Java 基础语法、面向对象、集合框架……"
}
```

**响应**

- **类型**：`Result<string>`
- 返回内容可能为：
  - 纯文本层级结构
  - Markdown 列表
  - 特定思维导图工具可解析的格式

---

## 八、认证相关接口（模块版 Auth）

> 控制器：`module/auth/controller/AuthController`  
> 基础路径：`/api/auth`  
> 鉴权：部分接口需要登录

### 8.1 POST /api/auth/login

- **说明**：账号密码登录，返回 Token。

**请求**

- **方法**：`POST`
- **URL**：`/api/auth/login`
- **请求体 LoginDTO**：

```json
{
  "username": "string", // 不可为空
  "password": "string"  // 不可为空
}
```

**响应**

- **类型**：`Result<TokenVO>`

---

### 8.2 GET /api/auth/send-sms-code

- **说明**：发送短信验证码。

- **方法**：`GET`
- **URL**：`/api/auth/send-sms-code`
- **查询参数**：
  - `mobile` (string, 必填，必须满足正则 `^1[3-9]\d{9}$`)

**响应**

- **类型**：`Result<string>`（data 通常为空或为提示信息）

---

### 8.3 POST /api/auth/sms-login

- **说明**：短信验证码登录。

**请求**

- **方法**：`POST`
- **URL**：`/api/auth/sms-login`
- **请求体 SmsLoginDTO**（字段示意）：

```json
{
  "mobile": "138xxxx1234",
  "code": "123456"
}
```

**响应**

- **类型**：`Result<TokenVO>`

---

### 8.4 POST /api/auth/wechat-login

- **说明**：使用微信登录。

**请求**

- **方法**：`POST`
- **URL**：`/api/auth/wechat-login`
- **请求体 WechatLoginDTO**（字段示意）：

```json
{
  "code": "wx-login-code"
}
```

**响应**

- **类型**：`Result<TokenVO>`

---

### 8.5 POST /api/auth/logout

- **说明**：退出登录。

**请求**

- **方法**：`POST`
- **URL**：`/api/auth/logout`
- **请求头**：
  - `Authorization: Bearer <token>`

**响应**

- **类型**：`Result<string>`（data 通常为空）

---

### 8.6 POST /api/auth/bind-mobile

- **说明**：登录后绑定手机号。

**请求**

- **方法**：`POST`
- **URL**：`/api/auth/bind-mobile`
- **请求头**：
  - `Authorization: Bearer <token>`
- **请求体 BindMobileDTO**（字段示意）：

```json
{
  "mobile": "138xxxx1234",
  "code": "123456"
}
```

**响应**

- **类型**：`Result<string>`

---

### 8.7 POST /api/auth/change-mobile

- **说明**：已绑定手机号用户，进行换绑。

**请求**

- **方法**：`POST`
- **URL**：`/api/auth/change-mobile`
- **请求头**：
  - `Authorization: Bearer <token>`
- **请求体 ChangeMobileDTO**（字段示意）：

```json
{
  "oldMobile": "138xxxx1111",
  "oldCode": "123456",
  "newMobile": "138xxxx2222",
  "newCode": "654321"
}
```

**响应**

- **类型**：`Result<string>`

---

## 九、用户管理接口（模块版 User）

> 控制器：`module/user/controller/UserController`  
> 基础路径：`/api/user`  
> 鉴权：部分接口需要登录

### 9.1 GET /api/user/page

- **说明**：分页查询用户列表（通常用于后台管理）。

**请求**

- **方法**：`GET`
- **URL**：`/api/user/page`
- **查询参数**：
  - `current` (number, 可选，默认：1) 当前页号
  - `size` (number, 可选，默认：10) 每页条数
  - `username` (string, 可选) 按用户名模糊查询

**响应**

- **类型**：`Result<PageResult<UserVO>>`
- `PageResult` 结构：

```json
{
  "records": [ /* UserVO[] */ ],
  "total": 100
}
```

---

### 9.2 GET /api/user/{id}

- **说明**：根据用户 ID 查询用户详情。

**请求**

- **方法**：`GET`
- **URL**：`/api/user/{id}`
- **路径参数**：
  - `id` (number, 必填)

**响应**

- **类型**：`Result<UserVO>`

---

### 9.3 GET /api/user/user-info

- **说明**：获取当前登录用户的基本信息。

**请求**

- **方法**：`GET`
- **URL**：`/api/user/user-info`
- **请求头**：
  - `Authorization: Bearer <token>`

**响应**

- **类型**：`Result<UserVO>`

---

### 9.4 POST /api/user

- **说明**：用户注册。

**请求**

- **方法**：`POST`
- **URL**：`/api/user`
- **请求体 UserDTO**（字段示意）：

```json
{
  "username": "user1",
  "password": "123456",
  "nickname": "张三",
  "gender": 1,
  "...": "其他注册所需字段"
}
```

**响应**

- **类型**：`Result<string>`

---

### 9.5 PUT /api/user/update-profile

- **说明**：登录后修改个人资料。

**请求**

- **方法**：`PUT`
- **URL**：`/api/user/update-profile`
- **请求头**：
  - `Authorization: Bearer <token>`
- **请求体 UserDTO**（修改的字段即可）：

```json
{
  "nickname": "新的昵称",
  "gender": 2,
  "...": "其他需要更新的字段"
}
```

**响应**

- **类型**：`Result<string>`

---

## 十、会话管理接口（Chat Session）

> 控制器：`ChatSessionController`  
> 基础路径：`/api/chat/session`  
> 鉴权：**全部需要 `Authorization`**

### 10.1 POST /api/chat/session/create

- **说明**：创建新的聊天会话。

**请求**

- **方法**：`POST`
- **URL**：`/api/chat/session/create`
- **请求头**：
  - `Authorization: Bearer <token>`
- **请求体 CreateSessionDTO**（字段示意）：

```json
{
  "title": "与校园助手的对话",
  "model": "qwen-turbo",
  "...": "其他会话配置字段"
}
```

**响应**

- **类型**：`Result<ChatSessionVO>`

---

### 10.2 PUT /api/chat/session/{id}/title

- **说明**：修改会话标题。

**请求**

- **方法**：`PUT`
- **URL**：`/api/chat/session/{id}/title`
- **请求头**：
  - `Authorization: Bearer <token>`
- **路径参数**：
  - `id` (number, 必填)：会话 ID
- **请求体 UpdateSessionDTO**（字段示意）：

```json
{
  "title": "新的会话标题"
}
```

**响应**

- **类型**：`Result<ChatSessionVO>`

---

### 10.3 GET /api/chat/session/{id}

- **说明**：获取会话详情，包含该会话下的所有消息。

**请求**

- **方法**：`GET`
- **URL**：`/api/chat/session/{id}`
- **请求头**：
  - `Authorization: Bearer <token>`
- **路径参数**：
  - `id` (number, 必填)

**响应**

- **类型**：`Result<ChatSessionVO>`

> `ChatSessionVO` 中通常包含会话基本信息和消息列表。

---

### 10.4 DELETE /api/chat/session/{id}

- **说明**：删除会话，并级联删除该会话下所有消息。

**请求**

- **方法**：`DELETE`
- **URL**：`/api/chat/session/{id}`
- **请求头**：
  - `Authorization: Bearer <token>`
- **路径参数**：
  - `id` (number, 必填)

**响应**

- **类型**：`Result<boolean>`
- `data = true` 表示删除成功。

---

### 10.5 GET /api/chat/session/list

- **说明**：获取当前登录用户的所有会话列表（不一定包含所有消息，只含摘要）。

**请求**

- **方法**：`GET`
- **URL**：`/api/chat/session/list`
- **请求头**：
  - `Authorization: Bearer <token>`

**响应**

- **类型**：`Result<List<ChatSessionVO>>`

---

## 十一、注意事项与前端封装建议

- **关于被注释的类 / 接口**
  - 如 `UserManagementController`、`ChatMessageController` 以及部分被 `//` 注释掉的方法，均视为当前版本未启用，**不应在前端进行接口封装**。
- **关于流式接口**
  - `Flux<String>` 返回的几个接口（问答流 / 搜索 / 图片分析），前端应使用 `EventSource`、`fetch + ReadableStream` 或对应框架的 SSE / 流式工具进行消费。
- **关于错误处理**
  - 除个别手动构造错误信息外，大多数失败会通过 `Result.code != 200` 体现，前端应统一拦截处理。

如果你需要，我可以再为每个接口补充 **前端 TypeScript 类型定义** 或 **封装好的请求函数模板**，方便直接在前端项目中使用。


