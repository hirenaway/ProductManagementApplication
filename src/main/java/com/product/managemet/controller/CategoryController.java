package com.product.managemet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.managemet.dto.CategoryDTO;
import com.product.managemet.dto.CategoryUpdateMessage;
import com.product.managemet.service.CategoryInterface;
import com.product.managemet.util.Constants;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final JmsTemplate jmsTemplate;

    private final CategoryInterface categoryService;

    public CategoryController(CategoryInterface categoryService, JmsTemplate jmsTemplate) {
        this.categoryService = categoryService;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@Validated @RequestBody CategoryDTO categoryDto) {
    	jmsTemplate.convertAndSend(Constants.CATEGORY_CREATE_QUEUE, categoryDto, message -> {
    	    message.setStringProperty("_type", "com.product.managemet.dto.CategoryDTO");
    	    return message;
    	});
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_CREATE_CATEGORY_RESPONSE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Validated @RequestBody CategoryDTO categoryDto) {
        CategoryUpdateMessage categoryUpdateMessage = new CategoryUpdateMessage(id, categoryDto);
        jmsTemplate.convertAndSend(Constants.CATEGORY_UPDATE_QUEUE, categoryUpdateMessage, message -> {
    	    message.setStringProperty("_type", "com.product.managemet.dto.CategoryUpdateMessage");
    	    return message;
    	});
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_UPDATE_CATEGORY_RESPONSE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        jmsTemplate.convertAndSend(Constants.CATEGORY_DELETE_QUEUE, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_DELETE_CATEGORY_RESPONSE);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
