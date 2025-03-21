package com.xupt.demo.service;

import com.xupt.demo.model.Account;
import com.xupt.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // 查询账户余额
    public BigDecimal getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("账户不存在"));
    }

    // 事务管理，转账操作
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("转账金额必须大于 0");
        }
        Account sender = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new RuntimeException("转出账户不存在"));
        Account receiver = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new RuntimeException("转入账户不存在"));
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }
        // 扣款
        sender.setBalance(sender.getBalance().subtract(amount));
        accountRepository.save(sender);
        // 存款
        receiver.setBalance(receiver.getBalance().add(amount));
        accountRepository.save(receiver);
    }
}
