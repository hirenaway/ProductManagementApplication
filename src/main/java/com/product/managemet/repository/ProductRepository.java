package com.product.managemet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.managemet.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
