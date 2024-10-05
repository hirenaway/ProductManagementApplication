package com.product.managemet.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.product.managemet.dto.ProductDTO;
import com.product.managemet.dto.ProductUpdateMessage;
import com.product.managemet.service.AuditLogsInterface;
import com.product.managemet.service.ProductInterface;
import com.product.managemet.util.Constants;

@Service
public class ProductListener {

    private final ProductInterface productService;

    private final AuditLogsInterface auditLogService;

    public ProductListener(ProductInterface productService, AuditLogsInterface auditLogService) {
        this.productService = productService;
        this.auditLogService = auditLogService;
    }

    @JmsListener(destination = Constants.PRODUCT_CREATE_QUEUE)
    public void processProductCreate(ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        auditLogService.logAudit(Constants.PRODUCT_KEY_WORD, Constants.CREATE, String.valueOf(createdProduct.getId()));
    }

    @JmsListener(destination = Constants.PRODUCT_UPDATE_QUEUE)
    public void processProductUpdate(ProductUpdateMessage productUpdateMessage) {
        Long productId = productUpdateMessage.getId();
        ProductDTO productDTO = productUpdateMessage.getProductDTO();
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        auditLogService.logAudit(Constants.PRODUCT_KEY_WORD, Constants.UPDATE, String.valueOf(updatedProduct.getId()));
    }

    @JmsListener(destination = Constants.PRODUCT_DELETE_QUEUE)
    public void processProductDelete(Long productId) {
        productService.deleteProduct(productId);
        auditLogService.logAudit(Constants.PRODUCT_KEY_WORD, Constants.DELETE, String.valueOf(productId));
    }

}
