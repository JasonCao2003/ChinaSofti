## **在线商城库存管理系统**

### **📌 项目简介**

本项目是一个在线商城的库存管理系统，旨在解决高并发环境下商品库存的管理问题。系统模拟了商品库存的购买操作，并通过两种常见的锁机制（乐观锁和悲观锁）来确保数据的一致性和可靠性。

- **乐观锁（Optimistic Locking）**：适用于正常情况下的库存操作，通过版本控制避免并发更新导致的库存超卖。
- **悲观锁（Pessimistic Locking）**：适用于秒杀等高并发场景，确保库存更新时其他线程无法修改数据，直到当前操作完成。

------

### **🛠️ 技术栈**

- **Spring Boot** - 快速开发 Web 应用
- **Spring Data JPA** - 数据库访问
- **MySQL** - 关系型数据库
- **Lombok** - 简化 Java 代码
- **JMeter / Postman** - 压力测试和接口测试

------

### **📂 目录结构**

```
inventory-management-system/
│── src/
│   ├── main/
│   │   ├── java/com/xupt/demo/
│   │   │   ├── controller/        # 控制器层 (API 接口)
│   │   │   ├── model/             # 实体类 (Product)
│   │   │   ├── repository/        # 数据访问层 (JPA)
│   │   │   ├── service/           # 业务逻辑层
│   │   │   ├── DemoApplication.java  # 启动类
│   │   ├── resources/
│   │   │   ├── application.yml    # 配置文件
│── pom.xml  # 依赖管理
│── README.md  # 说明文档
```

------

### **💾 数据库表**

#### **商品表（`product`）**

```sql
CREATE TABLE product
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id   VARCHAR(20) UNIQUE NOT NULL,
    product_name VARCHAR(100)       NOT NULL,
    stock        INT                NOT NULL,
    version      INT                NOT NULL
);
```

- **`product_id`**：商品编号，唯一标识商品。
- **`product_name`**：商品名称。
- **`stock`**：商品库存数量。
- **`version`**：乐观锁版本字段（适用于乐观锁机制）。

------

### **📜 API 说明**

#### **1️⃣ 查询商品库存**

- **URL**：`GET /products/{productId}/stock`

- 请求参数

    - `productId`：商品编号

- 示例请求

  ```http
  GET http://localhost:8080/products/12345/stock
  ```

- 响应示例

  ```json
  {
    "productId": "12345",
    "productName": "Example Product",
    "stock": 500
  }
  ```

#### **2️⃣ 购买商品（乐观锁）**

- **URL**：`POST /products/{productId}/purchase`

- 请求参数

    - `productId`：商品编号
    - `quantity`：购买数量

- 示例请求

  ```json
  {
    "quantity": 5
  }
  ```

- 响应示例

  ```json
  {
    "message": "购买成功",
    "remainingStock": 495
  }
  ```

#### **3️⃣ 秒杀商品（悲观锁）**

- **URL**：`POST /products/{productId}/seckill`

- 请求参数

    - `productId`：商品编号
    - `quantity`：秒杀数量

- 示例请求

  ```json
  {
    "quantity": 3
  }
  ```

- 响应示例

  ```json
  {
    "message": "秒杀成功",
    "remainingStock": 97
  }
  ```

------

### **⚙️ 锁机制说明**

#### **1. 乐观锁（Optimistic Locking）**

- 使用 `@Version` 注解在 `Product` 实体的 `version` 字段上，确保在每次更新库存时，只有版本号匹配的数据才能执行更新操作。
- 适用于常规的库存更新操作，保证数据一致性，避免竞争条件。

#### **2. 悲观锁（Pessimistic Locking）**

- 在秒杀场景中，使用 `@Lock(LockModeType.PESSIMISTIC_WRITE)` 来加锁，防止并发请求同时更新库存。
- 适用于高并发场景，确保每次库存操作只能被一个线程执行。

------

### **📊 JMeter 压力测试**

为了测试高并发场景，可以使用 **JMeter** 进行压力测试：

1. **线程组配置**：设置多个并发用户来模拟高并发请求。
2. **HTTP 请求配置**：配置 `GET /products/{productId}/stock` 和 `POST /products/{productId}/seckill` 等接口。
3. **测试运行**：观察两种锁机制（乐观锁和悲观锁）下的库存一致性和性能表现。

------

### **🔍 可能遇到的问题**

| 问题         | 解决方案                                                   |
|------------|--------------------------------------------------------|
| 高并发场景下库存超卖 | 使用悲观锁机制（`@Lock(LockModeType.PESSIMISTIC_WRITE)`）处理并发请求 |
| 乐观锁更新失败    | 确保客户端版本号正确，避免“版本冲突”导致的更新失败                             |
| 负载过大时性能下降  | 使用数据库索引、缓存和优化 SQL 查询来提升性能                              |

------

### **📜 结语**

本项目演示了如何使用 **Spring Boot + JPA** 和 **锁机制** 解决在线商城商品库存管理中的并发问题。系统支持 **乐观锁** 和 *
*悲观锁** 两种机制，适应不同场景的需求。如果有任何问题或改进建议，欢迎提交 Issue 或 PR！🚀