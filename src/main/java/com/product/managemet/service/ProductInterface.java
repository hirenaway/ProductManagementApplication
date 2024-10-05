package com.product.managemet.service;

import java.util.List;

import com.product.managemet.dto.ProductDTO;

public interface ProductInterface {

	ProductDTO createProduct(ProductDTO productDto);

	ProductDTO updateProduct(Long id, ProductDTO productDto);

	void deleteProduct(Long id);

	ProductDTO getProductById(Long id);

	List<ProductDTO> getAllProducts();

}
