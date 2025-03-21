package com.xupt.demo.controller;

import com.xupt.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 查询商品库存
    @GetMapping("/{productId}/stock")
    public ResponseEntity<Integer> getProductStock(@PathVariable Long productId) {
        Integer stock = productService.getProductStock(productId);
        return ResponseEntity.ok(stock);
    }

    // 购买商品（乐观锁）
    @PostMapping("/{productId}/purchase")
    public ResponseEntity<String> purchaseProduct(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            productService.purchaseProduct(productId, quantity);
            return ResponseEntity.ok("购买成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 秒杀场景（悲观锁）
    @PostMapping("/{productId}/seckill")
    public ResponseEntity<String> seckillProduct(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            productService.purchaseProductWithPessimisticLock(productId, quantity);
            return ResponseEntity.ok("秒杀成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
