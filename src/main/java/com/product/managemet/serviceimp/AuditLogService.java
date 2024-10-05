package com.product.managemet.serviceimp;

import com.product.managemet.entity.AuditLogs;
import com.product.managemet.repository.AuditLogRepository;
import com.product.managemet.service.AuditLogsInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuditLogService implements AuditLogsInterface {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public void logAudit(String entityName, String action, String entityId) {
        AuditLogs auditLog = new AuditLogs();
        auditLog.setEntityName(entityName);
        auditLog.setAction(action);
        auditLog.setEntityId(entityId);
        auditLog.setTimeDateTime(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }
}
