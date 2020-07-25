package io.learning.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.learning.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
