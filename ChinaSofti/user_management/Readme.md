# **用户管理系统**

## **项目简介**

本项目是一个基于 **Spring Boot + JPA + MySQL + Druid + JWT** 的用户管理系统，支持 **用户的增删改查（CRUD）**，并实现 *
*分页查询、排序*。所有请求与响应均采用 **JSON 格式**。

------

## **功能列表**

- **用户管理**
    - 创建用户
    - 查询用户（支持分页、排序）
    - 通过 ID 查询用户
    - 更新用户信息
    - 删除用户
- **分页 & 排序**
    - 通过 JSON 传递分页参数
    - 自定义排序字段

------

## **技术栈**

| 技术                  | 说明             |
|---------------------|----------------|
| **Spring Boot 3.x** | Web 开发框架       |
| **Spring Data JPA** | ORM 框架，简化数据库操作 |
| **MySQL 8.x**       | 关系型数据库         |
| **Druid**           | 高性能数据库连接池      |
| **Lombok**          | 简化 Java 代码     |
| **Maven**           | 构建工具           |

------

## **项目结构**

```
user-management/
│── src/
│   ├── main/
│   │   ├── java/com/xupt/demo/
│   │   │   ├── controller/     # 控制层
│   │   │   ├── model/          # JPA 实体类
│   │   │   ├── repository/     # 数据访问层 (JPA)
│   │   │   ├── service/        # 业务逻辑层
│   │   │   ├── utils/          # JWT 工具类
│── pom.xml                      # 依赖管理
│── application.yml               # 配置文件
```

------

## **环境要求**

- **JDK 17+**
- **Maven 3+**
- **MySQL 8+**

------

## **安装与运行**

### **1. 数据库配置**

在 MySQL 中创建数据库：

```sql
-- 创建数据库
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


-- 样例数据
INSERT INTO users (username, nickname, password, email, avatar, sex, birthday)
VALUES
    ('zhangsan', '张三', 'password123', 'zhangsan@example.com', NULL, '男', '1995-06-15'),
    ('lisi', '李四', 'password123', 'lisi@example.com', NULL, '女', '1998-08-20'),
    ('wangwu', '王五', 'password123', 'wangwu@example.com', NULL, '保密', '2000-01-01');

```

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_management?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
```

------

## **API 文档**

### **1. 用户管理**

#### **（1）创建用户**

- **接口**：`POST /users`
- **请求 JSON**

```
{
  "username": "zhangsan",
  "nickname": "张三",
  "password": "123456",
  "email": "zhangsan@example.com"
}
```

- **响应**

```
{
  "id": 1,
  "username": "zhangsan",
  "nickname": "张三",
  "email": "zhangsan@example.com"
}
```

------

#### **（2）分页查询用户**

- **接口**：`POST /users/list`
- **请求 JSON**

```
{
  "page": 0,
  "size": 5,
  "sortBy": "username"
}
```

- **响应**

```
{
  "content": [
    {
      "id": 1,
      "username": "zhangsan",
      "nickname": "张三",
      "email": "zhangsan@example.com"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 5
}
```

------

#### **（3）根据 ID 查询用户**

- **接口**：`GET /users/{id}`

------

#### **（4）更新用户**

- **接口**：`PUT /users/{id}`
- **请求 JSON**

```
{
  "nickname": "张三三",
  "email": "zhangsan_new@example.com"
}
```

------

#### **（5）删除用户**

- **接口**：`DELETE /users/{id}`
