package com.product.managemet.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.product.managemet.dto.CategoryDTO;
import com.product.managemet.dto.CategoryUpdateMessage;
import com.product.managemet.service.AuditLogsInterface;
import com.product.managemet.service.CategoryInterface;
import com.product.managemet.util.Constants;

@Service
public class CategoryListener {
    private final CategoryInterface categoryService;

    private final AuditLogsInterface auditLogService;

    public CategoryListener(CategoryInterface categoryService, AuditLogsInterface auditLogService) {
        this.categoryService = categoryService;
        this.auditLogService = auditLogService;
    }

    @JmsListener(destination = Constants.CATEGORY_CREATE_QUEUE)
    public void processCategoryCreate(CategoryDTO productDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(productDTO);
        auditLogService.logAudit(Constants.CATEGORY_KEY_WORD, Constants.CREATE, String.valueOf(createdCategory.getId()));
    }

    @JmsListener(destination = Constants.CATEGORY_UPDATE_QUEUE)
    public void processCategoryUpdate(CategoryUpdateMessage categoryUpdateMessage) {
        Long categoryId = categoryUpdateMessage.getId();
        CategoryDTO categoryDTO = categoryUpdateMessage.getCategoryDTO();
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        auditLogService.logAudit(Constants.CATEGORY_KEY_WORD, Constants.UPDATE, String.valueOf(updatedCategory.getId()));
    }

    @JmsListener(destination = Constants.CATEGORY_DELETE_QUEUE)
    public void processCategoryDelete(Long categoryId) {
        categoryService.deleteCategory(categoryId);
        auditLogService.logAudit(Constants.CATEGORY_KEY_WORD, Constants.DELETE, String.valueOf(categoryId));
    }
}
