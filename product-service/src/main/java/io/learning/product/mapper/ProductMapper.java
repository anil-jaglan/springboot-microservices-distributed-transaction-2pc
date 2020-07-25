package io.learning.product.mapper;

import org.springframework.beans.BeanUtils;

import io.learning.core.domain.Product;
import io.learning.product.domain.ProductEntity;

public class ProductMapper {

    public static Product map(ProductEntity product) {
        Product prod = new Product();
        BeanUtils.copyProperties(product, prod);
        return prod;
    }

    public static ProductEntity map(Product product) {
        ProductEntity prod = new ProductEntity();
        BeanUtils.copyProperties(product, prod);
        return prod;
    }

}
