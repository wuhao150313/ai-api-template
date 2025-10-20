-- 创建数据库
CREATE
DATABASE IF NOT EXISTS api_template;
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_0900_ai_ci;

USE
api_template;

-- 用户表
CREATE TABLE `sys_user`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(50) NOT NULL COMMENT '用户名',
    `password`    varchar(100) DEFAULT NULL COMMENT '密码',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT '真实姓名',
    `email`       varchar(100) DEFAULT NULL COMMENT '邮箱',
    `mobile`      varchar(20)  DEFAULT NULL COMMENT '手机号',
    `avatar`      varchar(200) DEFAULT NULL COMMENT '头像',
    `nickname`    varchar(50)  DEFAULT NULL COMMENT '昵称',
    `gender`      tinyint      DEFAULT '0' COMMENT '性别 0:未知 1:男 2:女',
    `wx_openid`   varchar(100) DEFAULT NULL COMMENT '微信openid',
    `status`      tinyint      DEFAULT '1' COMMENT '状态 0:禁用 1:正常',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     tinyint      DEFAULT '0' COMMENT '删除标记 0:未删除 1:已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`) USING BTREE,
    KEY           `idx_mobile` (`mobile`),
    KEY           `idx_wx_openid` (`wx_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 默认密码: 123456
INSERT INTO `sys_user` (`username`,
                        `password`,
                        `nickname`,
                        `mobile`,
                        `status`)
VALUES ('admin',
        '$2a$10$/ymWYgifpcgV.bRZYySYveiG0E7Q9h4A8bTpxm/9cEJd1aUZwIh26',
        '管理员',
        '13800138000',
        1);
