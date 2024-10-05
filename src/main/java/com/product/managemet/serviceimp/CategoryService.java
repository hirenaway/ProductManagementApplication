package com.product.managemet.serviceimp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.managemet.dto.CategoryDTO;
import com.product.managemet.entity.Category;
import com.product.managemet.entity.Product;
import com.product.managemet.exception.ResourceNotFoundException;
import com.product.managemet.repository.CategoryRepository;
import com.product.managemet.service.CategoryInterface;
import com.product.managemet.util.Constants;

@Service
public class CategoryService implements CategoryInterface {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);

        return mapToDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + id));
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + id));
        return mapToDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CategoryDTO mapToDto(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        List<Long> productIds = category.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        dto.setProductIds(productIds);
        return dto;
    }
}
