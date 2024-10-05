package com.product.managemet.service;

public interface AuditLogsInterface {

	void logAudit(String entityName, String action, String entityId);

}
