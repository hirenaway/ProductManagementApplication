package com.product.managemet.service;

import java.util.List;

import com.product.managemet.dto.CategoryDTO;

public interface CategoryInterface {

	CategoryDTO createCategory(CategoryDTO categoryDto);

	CategoryDTO updateCategory(Long id, CategoryDTO categoryDto);

	void deleteCategory(Long id);

	CategoryDTO getCategoryById(Long id);

	List<CategoryDTO> getAllCategories();

}
