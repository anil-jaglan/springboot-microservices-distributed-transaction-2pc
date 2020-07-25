package io.learning.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.learning.core.domain.Product;
import io.learning.product.devil.ProductNotFoundException;
import io.learning.product.service.EventBus;
import io.learning.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * Exposes REST API Interface for interacting with ProductService.
 *
 * @author Anil Jaglan
 * @version 1.0
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private EventBus eventBus;

    @PostMapping
    @Operation(summary = "Create a new product")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find product by ID")
    public Product findById(@PathVariable("id") Long productId) {
        return productService.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found for id: " + productId));
    }

    @PutMapping("/{id}/quantity/{quantity}")
    @Operation(summary = "Update product quantity")
    public Product updateQuantity(@RequestHeader("X-Txn-ID") String transactionId, @PathVariable("id") Long productId, @PathVariable("quantity") int quantity) {
        productService.updateQuantity(transactionId, productId, quantity);
        return eventBus.receiveEvent(transactionId).getProduct();
    }

}
