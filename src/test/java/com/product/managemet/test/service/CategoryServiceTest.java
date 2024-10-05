package com.product.managemet.test.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.product.managemet.dto.CategoryDTO;
import com.product.managemet.entity.Category;
import com.product.managemet.exception.ResourceNotFoundException;
import com.product.managemet.repository.CategoryRepository;
import com.product.managemet.serviceimp.CategoryService;
import com.product.managemet.util.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electronics");
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        assertNotNull(createdCategory);
        assertEquals("Electronics", createdCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCategoryFailure() {
        when(categoryRepository.save(any(Category.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.createCategory(categoryDTO));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testUpdateCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO updatedCategory = categoryService.updateCategory(1L, categoryDTO);

        assertNotNull(updatedCategory);
        assertEquals("Electronics", updatedCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                categoryService.updateCategory(1L, categoryDTO));

        assertEquals(Constants.CATEGORY_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testUpdateCategoryFailure() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.updateCategory(1L, categoryDTO));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testDeleteCategory() {
        when(categoryRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteCategory(1L));

        assertEquals(Constants.CATEGORY_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testDeleteCategoryFailure() {
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(categoryRepository).deleteById(anyLong());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.deleteCategory(1L));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryDTO foundCategory = categoryService.getCategoryById(1L);

        assertNotNull(foundCategory);
        assertEquals("Electronics", foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                categoryService.getCategoryById(1L));

        assertEquals(Constants.CATEGORY_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testGetCategoryByIdFailure() {
        when(categoryRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.getCategoryById(1L));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<CategoryDTO> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Electronics", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCategoriesFailure() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.getAllCategories());

        assertEquals("Database error", exception.getMessage());
    }
}

