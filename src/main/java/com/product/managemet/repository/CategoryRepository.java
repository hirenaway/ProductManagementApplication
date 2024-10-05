package com.product.managemet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.managemet.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
