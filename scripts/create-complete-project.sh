#!/bin/bash

# Spring Boot API 项目完整创建脚本
# 此脚本将一键创建完整的项目结构和所有文件

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
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

print_header() {
    echo -e "${PURPLE}========================================${NC}"
    echo -e "${PURPLE}$1${NC}"
    echo -e "${PURPLE}========================================${NC}"
}

# 检查脚本执行权限
check_permissions() {
    if [ ! -x "$1" ]; then
        print_info "为脚本添加执行权限: $1"
        chmod +x "$1"
    fi
}

# 显示欢迎信息
show_welcome() {
    clear
    echo -e "${CYAN}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║                                                              ║"
    echo "║        Spring Boot 3.5.5 API 项目自动创建脚本              ║"
    echo "║                                                              ║"
    echo "║   此脚本将创建完整的企业级 API 项目，包含：                 ║"
    echo "║   - Spring Boot 3.5.5                                       ║"
    echo "║   - Spring Security + JWT 认证                             ║"
    echo "║   - MyBatis-Plus + MySQL 数据库                             ║"
    echo "║   - Redis 缓存                                             ║"
    echo "║   - Knife4j API 文档                                        ║"
    echo "║   - Docker 部署支持                                        ║"
    echo "║   - 完整的代码结构                                          ║"
    echo "║                                                              ║"
    echo "║   作者: mqxu                                                ║"
    echo "║   版本: 1.0.0                                              ║"
    echo "║                                                              ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
    echo
}

# 显示项目信息
show_project_info() {
    print_header "项目信息"
    echo -e "${CYAN}项目名称:${NC} api-template"
    echo -e "${CYAN}技术栈:${NC} Spring Boot 3.5.5 + Spring Security + JWT + MyBatis-Plus + MySQL + Redis"
    echo -e "${CYAN}项目结构:${NC}"
    echo "api-template/"
    echo "├── src/main/java/top/mqxu/api/"
    echo "│   ├── common/           # 公共模块"
    echo "│   ├── config/           # 配置类"
    echo "│   ├── infrastructure/   # 基础设施层"
    echo "│   ├── module/           # 业务模块"
    echo "│   │   ├── auth/         # 认证模块"
    echo "│   │   └── user/         # 用户模块"
    echo "│   └── Application.java  # 启动类"
    echo "├── src/main/resources/   # 配置文件"
    echo "├── sql/                  # 数据库脚本"
    echo "├── scripts/              # 脚本文件"
    echo "├── docs/                 # 文档"
    echo "├── Dockerfile"
    echo "├── docker-compose.yml"
    echo "└── pom.xml"
    echo
}

# 执行脚本步骤
execute_script_step() {
    local script_name="$1"
    local description="$2"
    local script_path="scripts/$script_name"

    print_header "$description"

    if [ ! -f "$script_path" ]; then
        print_error "脚本文件不存在: $script_path"
        return 1
    fi

    # 检查执行权限
    check_permissions "$script_path"

    print_info "执行脚本: $script_path"
    if bash "$script_path"; then
        print_success "$description 完成"
        return 0
    else
        print_error "$description 失败"
        return 1
    fi
}

# 显示完成后的步骤
show_next_steps() {
    print_header "后续步骤"
    echo -e "${CYAN}项目文件已创建完成！请按照以下步骤完成项目搭建：${NC}"
    echo
    echo -e "${YELLOW}步骤 1: 配置敏感信息${NC}"
    echo "cp src/main/resources/application-secret.properties.example src/main/resources/application-secret.properties"
    echo "nano src/main/resources/application-secret.properties  # 填入实际配置"
    echo
    echo -e "${YELLOW}步骤 2: 创建数据库${NC}"
    echo "mysql -u root -p"
    echo "source sql/schema.sql"
    echo
    echo -e "${YELLOW}步骤 3: 启动项目${NC}"
    echo "mvn spring-boot:run"
    echo
    echo -e "${YELLOW}步骤 4: 访问应用${NC}"
    echo "API 文档: http://localhost:8083/doc.html"
    echo "健康检查: http://localhost:8083/actuator/health"
    echo
    echo -e "${YELLOW}步骤 5: Docker 部署 (可选)${NC}"
    echo "cp .env.example .env"
    echo "nano .env  # 填入环境变量"
    echo "docker-compose up -d"
    echo
    echo -e "${YELLOW}默认登录信息:${NC}"
    echo "用户名: admin"
    echo "密码: 123456"
    echo
}

# 主函数
main() {
    show_welcome
    show_project_info

    # 确认是否继续
    echo
    read -p "是否开始创建项目？(y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "操作已取消"
        exit 0
    fi

    # 确保 scripts 目录存在
    if [ ! -d "scripts" ]; then
        mkdir -p scripts
        print_info "创建 scripts 目录"
    fi

    local start_time=$(date +%s)

    # 执行各个创建步骤
    local step1_success=false
    local step2_success=false
    local step3_success=false

    if execute_script_step "create-project.sh" "步骤 1: 创建基础项目结构"; then
        step1_success=true
    fi

    echo
    read -p "是否继续创建 Java 类文件？(y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if execute_script_step "create-java-files.sh" "步骤 2: 创建 Java 类文件"; then
            step2_success=true
        fi
    fi

    echo
    read -p "是否继续创建业务模块文件？(y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if execute_script_step "create-business-files.sh" "步骤 3: 创建业务模块文件"; then
            step3_success=true
        fi
    fi

    # 计算执行时间
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))

    # 显示执行结果
    print_header "执行结果"
    if $step1_success && $step2_success && $step3_success; then
        print_success "所有步骤执行成功！"
        print_success "项目创建完成，耗时: ${duration} 秒"
        echo
        show_next_steps
    else
        print_error "部分步骤执行失败，请检查错误信息"
        echo
        if ! $step1_success; then
            print_error "步骤 1 失败: 基础项目结构创建失败"
        fi
        if ! $step2_success; then
            print_error "步骤 2 失败: Java 类文件创建失败"
        fi
        if ! $step3_success; then
            print_error "步骤 3 失败: 业务模块文件创建失败"
        fi
        echo
        print_info "请检查错误信息并手动修复问题"
        exit 1
    fi

    # 显示文件统计
    print_header "文件统计"
    local total_files=$(find . -type f -name "*.java" -o -name "*.xml" -o -name "*.yml" -o -name "*.properties" -o -name "*.sql" -o -name "*.sh" -o -name "Dockerfile" -o -name "*.md" | grep -v target | wc -l)
    local java_files=$(find . -name "*.java" | grep -v target | wc -l)
    local config_files=$(find . -name "*.xml" -o -name "*.yml" -o -name "*.properties" | grep -v target | wc -l)

    echo -e "${CYAN}总文件数:${NC} $total_files"
    echo -e "${CYAN}Java 文件:${NC} $java_files"
    echo -e "${CYAN}配置文件:${NC} $config_files"
    echo

    print_success "脚本执行完成！"
}

# 检查是否在正确的目录
check_directory() {
    if [ -f "pom.xml" ] && [ -d "src" ]; then
        print_warning "检测到已存在的项目，脚本将在当前目录创建/覆盖文件"
        echo
        read -p "是否继续？(y/n): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_info "操作已取消"
            exit 0
        fi
    fi
}

# 执行检查
check_directory

# 执行主函数
main "$@"