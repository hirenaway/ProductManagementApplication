package com.product.managemet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.managemet.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
