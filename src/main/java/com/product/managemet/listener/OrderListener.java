package com.product.managemet.listener;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.product.managemet.dto.OrderDTO;
import com.product.managemet.dto.OrderUpdateMessage;
import com.product.managemet.service.AuditLogsInterface;
import com.product.managemet.service.OrderInterface;
import com.product.managemet.util.Constants;

@Service
public class OrderListener {

    private final OrderInterface orderService;

    private final AuditLogsInterface auditLogService;

    public OrderListener(OrderInterface orderService, AuditLogsInterface auditLogService) {
        this.orderService = orderService;
        this.auditLogService = auditLogService;
    }

    @JmsListener(destination = Constants.ORDER_CREATE_QUEUE)
    public void processOrderCreate(OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        auditLogService.logAudit(Constants.ORDER_KEY_WORD, Constants.CREATE, String.valueOf(createdOrder.getId()));
    }

    @JmsListener(destination = Constants.ORDER_UPDATE_QUEUE)
    public void processOrderUpdate(OrderUpdateMessage orderUpdateMessage) {
        Long orderId = orderUpdateMessage.getId();
        OrderDTO orderDTO = orderUpdateMessage.getOrderDTO();
        OrderDTO updatedOrder = orderService.updateOrder(orderId, orderDTO);
        auditLogService.logAudit(Constants.ORDER_KEY_WORD, Constants.UPDATE, String.valueOf(updatedOrder.getId()));
    }

    @JmsListener(destination = Constants.ORDER_DELETE_QUEUE)
    public void processOrderDelete(Long orderId) {
        orderService.deleteOrder(orderId);
        auditLogService.logAudit(Constants.ORDER_KEY_WORD, Constants.DELETE, String.valueOf(orderId));
    }
}
