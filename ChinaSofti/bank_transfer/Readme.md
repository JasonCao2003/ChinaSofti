## **银行账户转账系统**

### **📌 项目简介**

本项目是一个基于 **Spring Boot + JPA** 的银行账户转账系统，模拟银行账户之间的资金转移。系统使用 **Spring 事务管理**，确保数据一致性，并保证在高并发场景下数据的安全性和正确性。

------

### **📂 目录结构**

```
bank-transfer-system/
│── src/
│   ├── main/
│   │   ├── java/com/xupt/demo/
│   │   │   ├── controller/           # 控制器层 (API 接口)
│   │   │   ├── model/                # 实体类 (Account)
│   │   │   ├── repository/           # 数据访问层 (JPA)
│   │   │   ├── service/              # 业务逻辑层
│   │   │   ├── DemoApplication.java  # 启动类
│   │   ├── resources/
│   │   │   ├── application.yml       # 配置文件
│── pom.xml  # 依赖管理
│── README.md  # 说明文档
```

------

### **🛠️ 技术栈**

- **Spring Boot** - 快速开发 Web 应用
- **Spring Data JPA** - 数据库访问
- **Spring Transactional** - 事务管理
- **H2 / MySQL** - 关系型数据库
- **Lombok** - 简化 Java 代码

------

### **💾 数据库表**

#### **账户表（`account`）**

```sql
CREATE TABLE account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_name VARCHAR(50) NOT NULL,
    balance DECIMAL(15,2) NOT NULL
);
```

------

### **📜 API 说明**

#### **1️⃣ 查询账户余额**

- **URL**：`GET /accounts/{accountNumber}/balance`

- 示例请求

  ```http
  GET http://localhost:8080/accounts/123456/balance
  ```

- 响应示例

  ```json
  {
    "accountNumber": "123456",
    "accountName": "Alice",
    "balance": 5000.00
  }
  ```

#### **2️⃣ 账户转账**

- **URL**：`POST /accounts/transfer`

- 请求示例

  ```json
  {
    "fromAccount": "123456",
    "toAccount": "789012",
    "amount": 500.00
  }
  ```

- 响应示例

  ```json
  {
    "message": "转账成功",
    "fromAccountBalance": 4500.00,
    "toAccountBalance": 2500.00
  }
  ```

------

### **⚙️ 事务管理**

本项目使用 **`@Transactional`** 处理转账逻辑，确保：

1. **转账操作的原子性**：要么完全执行，要么回滚，避免资金丢失。
2. 事务隔离级别：`REPEATABLE_READ`
    - 确保在同一事务内，多次查询同一账户的余额，数据不会发生变化，避免“脏读”或“幻读”问题。

------

### **📊 JMeter 测试**

为了测试高并发转账情况，可以使用 **JMeter** 进行压力测试：

1. 创建 **Thread Group**，设置并发用户数。
2. 配置 **HTTP Request**，调用 `/accounts/transfer`。
3. 运行测试，观察事务是否正确处理并发场景。

------

### **🔍 可能遇到的问题**

| 问题                   | 解决方案                                                     |
| ---------------------- | ------------------------------------------------------------ |
| 余额不足但仍然转账成功 | 确保在 `@Transactional` 方法内正确检查余额，避免未提交事务时余额不足的情况 |
| 高并发下出现资金丢失   | 可以使用 **悲观锁**（`@Lock(LockModeType.PESSIMISTIC_WRITE)`）防止并发更新 |
| HTTP 400/500 错误      | 检查请求参数、数据库连接是否正确                             |

------

### **📜 结语**

本项目演示了如何使用 **Spring Boot + JPA + 事务管理** 构建银行账户转账系统。它确保数据一致性，避免资金损失，同时支持 **高并发场景** 处理。如果有任何问题或改进建议，欢迎提交 Issue 或 PR！🚀