#!/bin/bash

# Spring Boot API 项目空结构一键创建脚本
# 此脚本将创建完整的项目空文件结构

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
    echo "║        Spring Boot 3.5.5 API 项目空结构创建脚本              ║"
    echo "║                                                              ║"
    echo "║   此脚本将创建完整的企业级 API 项目空文件结构：               ║"
    echo "║   - 完整的目录结构                                          ║"
    echo "║   - 所有配置文件（空）                                      ║"
    echo "║   - 所有 Java 类文件（空）                                 ║"
    echo "║   - Docker 配置文件（空）                                   ║"
    echo "║   - 数据库脚本（空）                                        ║"
    echo "║                                                              ║"
    echo "║   文件内容需要按照教程文档手动填充                           ║"
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
    echo -e "${YELLOW}注意: 此脚本只创建空文件，需要手动填充内容${NC}"
    echo
}

# 执行空文件创建脚本
execute_empty_project_script() {
    local script_path="scripts/create-project-empty.sh"

    print_header "创建项目空文件结构"

    if [ ! -f "$script_path" ]; then
        print_error "脚本文件不存在: $script_path"
        return 1
    fi

    # 检查执行权限
    check_permissions "$script_path"

    print_info "执行空文件创建脚本: $script_path"
    if bash "$script_path"; then
        print_success "项目空文件结构创建完成"
        return 0
    else
        print_error "项目空文件结构创建失败"
        return 1
    fi
}

# 显示完成后的步骤
show_next_steps() {
    print_header "后续步骤"
    echo -e "${CYAN}项目空文件结构已创建完成！请按照以下步骤完成项目搭建：${NC}"
    echo
    echo -e "${YELLOW}步骤 1: 填充配置文件${NC}"
    echo "按照 docs/从零搭建教程.md 中的内容填充以下配置文件："
    echo "- pom.xml"
    echo "- src/main/resources/application.yml"
    echo "- src/main/resources/application-dev.yml"
    echo "- src/main/resources/application-prod.yml"
    echo "- src/main/resources/application-secret.properties"
    echo "- src/main/resources/logback-spring.xml"
    echo
    echo -e "${YELLOW}步骤 2: 填充数据库脚本${NC}"
    echo "按照教程填充 sql/schema.sql 文件"
    echo
    echo -e "${YELLOW}步骤 3: 填充 Docker 配置${NC}"
    echo "按照教程填充以下文件："
    echo "- Dockerfile"
    echo "- docker-compose.yml"
    echo "- .env.example"
    echo "- .gitignore"
    echo
    echo -e "${YELLOW}步骤 4: 填充 Java 类文件${NC}"
    echo "按照教程顺序填充 Java 类文件："
    echo "1. 公共模块 (common)"
    echo "2. 配置类 (config)"
    echo "3. 基础设施层 (infrastructure)"
    echo "4. 认证模块 (auth)"
    echo "5. 用户模块 (user)"
    echo
    echo -e "${YELLOW}步骤 5: 配置数据库${NC}"
    echo "mysql -u root -p"
    echo "source sql/schema.sql"
    echo
    echo -e "${YELLOW}步骤 6: 启动项目${NC}"
    echo "mvn spring-boot:run"
    echo
    echo -e "${YELLOW}步骤 7: 访问应用${NC}"
    echo "API 文档: http://localhost:8083/doc.html"
    echo "健康检查: http://localhost:8083/actuator/health"
    echo
    echo -e "${YELLOW}教程文档:${NC} docs/从零搭建教程.md"
    echo
}

# 主函数
main() {
    show_welcome
    show_project_info

    # 确认是否继续
    echo
    read -p "是否开始创建项目空文件结构？(y/n): " -n 1 -r
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

    # 执行空文件创建脚本
    if execute_empty_project_script; then
        # 计算执行时间
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))

        # 显示执行结果
        print_header "执行结果"
        print_success "空文件结构创建成功！"
        print_success "项目创建完成，耗时: ${duration} 秒"
        echo
        show_next_steps

        # 显示文件统计
        print_header "文件统计"
        local total_dirs=$(find . -type d | grep -v target | grep -v .git | wc -l)
        local empty_files=$(find . -type f -size 0 | grep -v target | grep -v .git | wc -l)

        echo -e "${CYAN}目录数量:${NC} $total_dirs"
        echo -e "${CYAN}空文件数量:${NC} $empty_files"
        echo

        print_success "脚本执行完成！"
    else
        print_error "空文件结构创建失败，请检查错误信息"
        exit 1
    fi
}

# 检查是否在正确的目录
check_directory() {
    if [ -f "pom.xml" ] && [ -d "src" ]; then
        print_warning "检测到已存在的项目，脚本将在当前目录创建/覆盖空文件"
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