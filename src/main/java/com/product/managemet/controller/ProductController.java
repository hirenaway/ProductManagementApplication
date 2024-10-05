package com.product.managemet.controller;

import com.product.managemet.dto.ProductDTO;
import com.product.managemet.dto.ProductUpdateMessage;
import com.product.managemet.service.ProductInterface;
import com.product.managemet.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final JmsTemplate jmsTemplate;
    private final ProductInterface productService;

    public ProductController(JmsTemplate jmsTemplate, ProductInterface productService) {
        this.jmsTemplate = jmsTemplate;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@Validated @RequestBody ProductDTO productDTO) {
    	jmsTemplate.convertAndSend(Constants.PRODUCT_CREATE_QUEUE, productDTO, message -> {
    	    message.setStringProperty("_type", "com.product.managemet.dto.ProductDTO");
    	    return message;
    	});
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_CREATE_PRODUCT_RESPONSE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Validated @RequestBody ProductDTO productDTO) {
        ProductUpdateMessage productUpdateMessage = new ProductUpdateMessage(id, productDTO);
        jmsTemplate.convertAndSend(Constants.PRODUCT_UPDATE_QUEUE, productUpdateMessage, message -> {
    	    message.setStringProperty("_type", "com.product.managemet.dto.ProductUpdateMessage");
    	    return message;
    	});
        jmsTemplate.convertAndSend(Constants.PRODUCT_UPDATE_QUEUE, productUpdateMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_UPDATE_PRODUCT_RESPONSE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        jmsTemplate.convertAndSend(Constants.PRODUCT_DELETE_QUEUE, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_DELETE_PRODUCT_RESPONSE);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
