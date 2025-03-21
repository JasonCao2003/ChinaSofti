-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE user_db;

-- 创建 users 表
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                       nickname VARCHAR(50) NOT NULL COMMENT '昵称',
                       password VARCHAR(255) NOT NULL COMMENT '密码',
                       email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
                       avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
                       sex ENUM('男', '女', '保密') DEFAULT '保密' COMMENT '性别',
                       birthday DATE DEFAULT NULL COMMENT '生日',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


INSERT INTO users (username, nickname, password, email, avatar, sex, birthday)
VALUES
    ('zhangsan', '张三', 'password123', 'zhangsan@example.com', NULL, '男', '1995-06-15'),
    ('lisi', '李四', 'password123', 'lisi@example.com', NULL, '女', '1998-08-20'),
    ('wangwu', '王五', 'password123', 'wangwu@example.com', NULL, '保密', '2000-01-01');
