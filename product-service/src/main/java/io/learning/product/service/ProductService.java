package io.learning.product.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.learning.core.domain.Product;
import io.learning.product.devil.ProductProcessingException;
import io.learning.product.event.ProductTransactionEvent;
import io.learning.product.mapper.ProductMapper;
import io.learning.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Product createProduct(Product product) {
        return ProductMapper.map(productRepository.save(ProductMapper.map(product)));
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId).map(ProductMapper::map);
    }

    @Async
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateQuantity(String transactionId, Long productId, int quantity) {
        log.info("Updating product quantity with id: {}, quantity: {}", productId, quantity);
        findById(productId).ifPresent(prod -> {
            if (prod.getQuantity() < quantity) {
                throw new ProductProcessingException("Insufficient product quantity. Available: " + prod.getQuantity() + ", Demand: " + quantity);
            }
            prod.setQuantity(prod.getQuantity() - quantity);
            eventPublisher.publishEvent(new ProductTransactionEvent(transactionId, prod));
            productRepository.save(ProductMapper.map(prod));
        });
    }

}
