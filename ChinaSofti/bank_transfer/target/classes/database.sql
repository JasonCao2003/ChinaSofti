CREATE DATABASE bank_system;
USE bank_system;


CREATE TABLE accounts
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,           -- 账户ID（主键，自增）
    account_number VARCHAR(20)    NOT NULL UNIQUE,              -- 账户编号（唯一）
    account_name   VARCHAR(50)    NOT NULL,                     -- 账户名称
    balance        DECIMAL(15, 2) NOT NULL CHECK (balance >= 0) -- 账户余额（不能为负）
);


INSERT INTO accounts (account_number, account_name, balance)
VALUES ('123456', '张三', 1000.00),
       ('789012', '李四', 500.00);