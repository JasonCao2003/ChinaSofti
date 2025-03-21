package com.xupt.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;  // 账户编号

    @Column(nullable = false)
    private String accountName;  // 账户名

    @Column(nullable = false)
    private BigDecimal balance;  // 余额

}
