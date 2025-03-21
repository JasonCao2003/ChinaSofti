CREATE DATABASE products;
USE products;


CREATE TABLE product
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,        -- 商品ID（主键）
    product_code VARCHAR(50)  NOT NULL UNIQUE,             -- 商品编号（唯一）
    product_name VARCHAR(100) NOT NULL,                    -- 商品名称
    stock        INT          NOT NULL CHECK (stock >= 0), -- 商品库存（不可为负）
    version      INT          NOT NULL DEFAULT 0           -- 版本号（用于乐观锁）
);


INSERT INTO product (product_code, product_name, stock, version)
VALUES ('P1001', '苹果手机', 100, 0),
       ('P1002', '华为手机', 50, 0);
