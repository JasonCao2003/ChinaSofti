package com.xupt.demo.service;

import com.xupt.demo.model.Product;
import com.xupt.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 查询商品库存
    public Integer getProductStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        return product.getStock();
    }

    // 使用乐观锁进行库存购买
    @Transactional
    public void purchaseProduct(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        product.setStock(product.getStock() - quantity);
        try {
            productRepository.save(product); // 更新商品
        } catch (OptimisticLockException e) {
            throw new RuntimeException("并发更新冲突，请重试");
        }
    }

    // 使用悲观锁进行库存购买（秒杀场景）
    @Transactional
    public void purchaseProductWithPessimisticLock(Long productId, int quantity) {
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
