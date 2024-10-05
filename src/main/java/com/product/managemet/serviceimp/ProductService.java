package com.product.managemet.serviceimp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.managemet.dto.ProductDTO;
import com.product.managemet.entity.Category;
import com.product.managemet.entity.Product;
import com.product.managemet.exception.ResourceNotFoundException;
import com.product.managemet.repository.CategoryRepository;
import com.product.managemet.repository.ProductRepository;
import com.product.managemet.service.ProductInterface;
import com.product.managemet.util.Constants;

@Service
public class ProductService implements ProductInterface {
	
	private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

	@Override
	@Transactional
	public ProductDTO createProduct(ProductDTO productDto) {
		Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + productDto.getCategoryId()));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
	}

	@Override
	@Transactional
	public ProductDTO updateProduct(Long id, ProductDTO productDto) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + id));

		if (productDto.getName() != null) {
			product.setName(productDto.getName());
		}
		if (productDto.getPrice() != null) {
			product.setPrice(productDto.getPrice());
		}
		if (productDto.getCategoryId() != null) {
			Category category = categoryRepository.findById(productDto.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + id));
			product.setCategory(category);
		}

		Product updatedProduct = productRepository.save(product);
		return mapToDto(updatedProduct);
	}

	@Override
	@Transactional
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + id);
		}
		productRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDTO getProductById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + id));
		return mapToDto(product);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll()
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}
	
	private ProductDTO mapToDto(Product product) {
		ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        } else {
            dto.setCategoryId(null); 
        }
        return dto;
    }

}
