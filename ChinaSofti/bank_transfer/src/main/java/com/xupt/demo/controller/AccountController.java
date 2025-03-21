package com.xupt.demo.controller;

import com.xupt.demo.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 查询余额 API
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(Map.of("accountNumber", accountNumber, "balance", balance));
    }

    // 转账 API
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody Map<String, Object> request) {
        String fromAccount = (String) request.get("fromAccount");
        String toAccount = (String) request.get("toAccount");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        accountService.transfer(fromAccount, toAccount, amount);
        return ResponseEntity.ok(Map.of("message", "转账成功"));
    }
}
