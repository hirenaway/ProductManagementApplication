package com.product.managemet.repository;

import com.product.managemet.entity.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLogs, Long> {
}
