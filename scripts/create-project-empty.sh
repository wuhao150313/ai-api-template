#!/bin/bash

# Spring Boot API 项目空文件创建脚本
# 此脚本只创建空文件，不填充内容

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

# 创建空文件的函数
create_empty_file() {
    local file_path="$1"

    # 确保目录存在
    local dir=$(dirname "$file_path")
    create_dir "$dir"

    # 如果文件已存在，跳过
    if [ -f "$file_path" ]; then
        print_warning "文件已存在，跳过: $file_path"
        return 0
    fi

    # 创建空文件
    touch "$file_path"
    print_success "创建空文件: $file_path"
}

# 主函数
main() {
    print_info "开始创建 Spring Boot API 项目空文件结构..."
    print_info "项目将创建在当前目录: $(pwd)"

    echo
    read -p "是否继续？(y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "操作已取消"
        exit 0
    fi

    print_info "开始创建项目结构..."
    echo

    # 1. 创建目录结构
    print_info "1. 创建项目目录结构..."

    # 主目录
    create_dir "src/main/java/top/mqxu/api"
    create_dir "src/main/resources"
    create_dir "src/main/resources/mapper"
    create_dir "src/test/java"
    create_dir "sql"
    create_dir "logs"
    create_dir "scripts"
    create_dir "docs"

    # 公共模块目录
    create_dir "src/main/java/top/mqxu/api/common/cache"
    create_dir "src/main/java/top/mqxu/api/common/entity"
    create_dir "src/main/java/top/mqxu/api/common/enums"
    create_dir "src/main/java/top/mqxu/api/common/exception"
    create_dir "src/main/java/top/mqxu/api/common/filter"
    create_dir "src/main/java/top/mqxu/api/common/handler"
    create_dir "src/main/java/top/mqxu/api/common/result"
    create_dir "src/main/java/top/mqxu/api/common/utils"
    create_dir "src/main/java/top/mqxu/api/common/model"

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

    print_success "目录结构创建完成"
    echo

    # 2. 创建配置文件
    print_info "2. 创建配置文件..."

    # Maven 配置
    create_empty_file "pom.xml"

    # 应用配置
    create_empty_file "src/main/resources/application.yml"
    create_empty_file "src/main/resources/application-dev.yml"
    create_empty_file "src/main/resources/application-prod.yml"
    create_empty_file "src/main/resources/application-secret.properties.example"
    create_empty_file "src/main/resources/logback-spring.xml"

    # 数据库脚本
    create_empty_file "sql/schema.sql"

    # Docker 配置
    create_empty_file "Dockerfile"
    create_empty_file "docker-compose.yml"
    create_empty_file ".env.example"
    create_empty_file ".gitignore"

    print_success "配置文件创建完成"
    echo

    # 3. 创建启动类
    print_info "3. 创建启动类..."
    create_empty_file "src/main/java/top/mqxu/api/Application.java"
    print_success "启动类创建完成"
    echo

    # 4. 创建公共模块文件
    print_info "4. 创建公共模块文件..."

    # 实体类
    create_empty_file "src/main/java/top/mqxu/api/common/entity/BaseEntity.java"

    # 枚举类
    create_empty_file "src/main/java/top/mqxu/api/common/enums/DeletedEnum.java"
    create_empty_file "src/main/java/top/mqxu/api/common/enums/GenderEnum.java"
    create_empty_file "src/main/java/top/mqxu/api/common/enums/StatusEnum.java"

    # 响应结果
    create_empty_file "src/main/java/top/mqxu/api/common/result/ResultCode.java"
    create_empty_file "src/main/java/top/mqxu/api/common/result/Result.java"
    create_empty_file "src/main/java/top/mqxu/api/common/result/PageResult.java"

    # 异常处理
    create_empty_file "src/main/java/top/mqxu/api/common/exception/ServerException.java"
    create_empty_file "src/main/java/top/mqxu/api/common/exception/GlobalExceptionHandler.java"

    # 缓存
    create_empty_file "src/main/java/top/mqxu/api/common/cache/RedisKeys.java"
    create_empty_file "src/main/java/top/mqxu/api/common/cache/RedisCache.java"

    # 工具类
    create_empty_file "src/main/java/top/mqxu/api/common/utils/JwtUtils.java"
    create_empty_file "src/main/java/top/mqxu/api/common/utils/SecurityUtils.java"

    # 过滤器
    create_empty_file "src/main/java/top/mqxu/api/common/filter/LogFilter.java"
    create_empty_file "src/main/java/top/mqxu/api/common/filter/JwtAuthenticationFilter.java"

    # 处理器
    create_empty_file "src/main/java/top/mqxu/api/common/handler/SecurityExceptionHandler.java"

    # 模型
    create_empty_file "src/main/java/top/mqxu/api/common/model/CustomUserDetails.java"

    print_success "公共模块文件创建完成"
    echo

    # 5. 创建配置类文件
    print_info "5. 创建配置类文件..."

    create_empty_file "src/main/java/top/mqxu/api/config/MyBatisPlusConfig.java"
    create_empty_file "src/main/java/top/mqxu/api/config/RedisConfig.java"
    create_empty_file "src/main/java/top/mqxu/api/config/JacksonConfig.java"
    create_empty_file "src/main/java/top/mqxu/api/config/Knife4jConfig.java"
    create_empty_file "src/main/java/top/mqxu/api/config/SecurityConfig.java"

    print_success "配置类文件创建完成"
    echo

    # 6. 创建基础设施层文件
    print_info "6. 创建基础设施层文件..."

    # 短信服务
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/sms/RongLianSmsProperties.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/sms/SmsProvider.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/sms/impl/RongLianSmsServiceImpl.java"

    # 对象存储
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/oss/AliyunOssProperties.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/oss/OssService.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/oss/impl/AliyunOssServiceImpl.java"

    # 微信服务
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/wechat/WechatProperties.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/wechat/WechatUserInfo.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/wechat/WechatService.java"
    create_empty_file "src/main/java/top/mqxu/api/infrastructure/wechat/impl/WechatServiceImpl.java"

    print_success "基础设施层文件创建完成"
    echo

    # 7. 创建认证模块文件
    print_info "7. 创建认证模块文件..."

    # DTO
    create_empty_file "src/main/java/top/mqxu/api/module/auth/model/dto/LoginDTO.java"
    create_empty_file "src/main/java/top/mqxu/api/module/auth/model/dto/SmsLoginDTO.java"
    create_empty_file "src/main/java/top/mqxu/api/module/auth/model/dto/WechatLoginDTO.java"
    create_empty_file "src/main/java/top/mqxu/api/module/auth/model/dto/BindMobileDTO.java"
    create_empty_file "src/main/java/top/mqxu/api/module/auth/model/dto/ChangeMobileDTO.java"

    # VO
    create_empty_file "src/main/java/top/mqxu/api/module/auth/model/vo/TokenVO.java"

    # Service
    create_empty_file "src/main/java/top/mqxu/api/module/auth/service/AuthService.java"
    create_empty_file "src/main/java/top/mqxu/api/module/auth/service/impl/AuthServiceImpl.java"

    # Controller
    create_empty_file "src/main/java/top/mqxu/api/module/auth/controller/AuthController.java"

    print_success "认证模块文件创建完成"
    echo

    # 8. 创建用户模块文件
    print_info "8. 创建用户模块文件..."

    # Entity
    create_empty_file "src/main/java/top/mqxu/api/module/user/model/entity/UserEntity.java"

    # DTO
    create_empty_file "src/main/java/top/mqxu/api/module/user/model/dto/UserDTO.java"

    # VO
    create_empty_file "src/main/java/top/mqxu/api/module/user/model/vo/UserVO.java"

    # Mapper
    create_empty_file "src/main/java/top/mqxu/api/module/user/mapper/UserMapper.java"

    # Convert
    create_empty_file "src/main/java/top/mqxu/api/module/user/convert/UserConvert.java"

    # Service
    create_empty_file "src/main/java/top/mqxu/api/module/user/service/UserService.java"
    create_empty_file "src/main/java/top/mqxu/api/module/user/service/impl/UserServiceImpl.java"

    # Controller
    create_empty_file "src/main/java/top/mqxu/api/module/user/controller/UserController.java"

    print_success "用户模块文件创建完成"
    echo

    # 9. 创建文档文件
    print_info "9. 创建文档文件..."
    create_empty_file "README.md"
    create_empty_file "docs/README.md"

    print_success "文档文件创建完成"
    echo

    # 统计信息
    print_info "项目空文件结构创建完成！"
    echo
    print_info "创建的统计信息："

    # 统计目录数
    local dir_count=$(find . -type d | grep -v target | grep -v .git | wc -l)
    print_info "- 目录数量: $dir_count"

    # 统计空文件数
    local file_count=$(find . -type f -size 0 | grep -v target | grep -v .git | wc -l)
    print_info "- 空文件数量: $file_count"

    echo
    print_info "项目结构已创建完成，请按照教程文档填充文件内容。"
    print_info "教程文档: docs/从零搭建教程.md"
    echo
    print_info "后续步骤："
    print_info "1. 按照 docs/从零搭建教程.md 填充文件内容"
    print_info "2. 配置 application-secret.properties"
    print_info "3. 创建数据库并执行 schema.sql"
    print_info "4. 启动项目"
}

# 执行主函数
main "$@"