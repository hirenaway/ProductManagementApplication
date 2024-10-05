package com.product.managemet.test.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.product.managemet.dto.ProductDTO;
import com.product.managemet.entity.Category;
import com.product.managemet.entity.Product;
import com.product.managemet.exception.ResourceNotFoundException;
import com.product.managemet.repository.CategoryRepository;
import com.product.managemet.repository.ProductRepository;
import com.product.managemet.serviceimp.ProductService;
import com.product.managemet.util.Constants;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Category A");

        product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(100.0);
        product.setCategory(category);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Product A");
        productDTO.setPrice(100.0);
        productDTO.setCategoryId(1L);
    }

    @Test
    void testCreateProduct() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO createdProduct = productService.createProduct(productDTO);

        assertNotNull(createdProduct);
        assertEquals(1L, createdProduct.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProductCategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.createProduct(productDTO));

        assertEquals(Constants.CATEGORY_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO updatedProduct = productService.updateProduct(1L, productDTO);

        assertNotNull(updatedProduct);
        assertEquals(1L, updatedProduct.getId());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(1L, productDTO));

        assertEquals(Constants.PRODUCT_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testUpdateProductCategoryNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(1L, productDTO));

        assertEquals(Constants.CATEGORY_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.deleteProduct(1L));

        assertEquals(Constants.PRODUCT_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testDeleteProductFailure() {
        when(productRepository.existsById(anyLong())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(productRepository).deleteById(anyLong());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.deleteProduct(1L));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductDTO foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.getProductById(1L));

        assertEquals(Constants.PRODUCT_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testGetProductByIdFailure() {
        when(productRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.getProductById(1L));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(1L, products.get(0).getId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProductsFailure() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.getAllProducts());

        assertEquals("Database error", exception.getMessage());
    }
}


