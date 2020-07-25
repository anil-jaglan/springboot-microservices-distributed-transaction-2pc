package io.learning.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.learning.product.domain.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
