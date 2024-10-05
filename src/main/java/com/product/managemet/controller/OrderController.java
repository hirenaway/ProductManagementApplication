package com.product.managemet.controller;

import com.product.managemet.dto.OrderDTO;
import com.product.managemet.dto.OrderUpdateMessage;
import com.product.managemet.service.OrderInterface;
import com.product.managemet.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final JmsTemplate jmsTemplate;

    private final OrderInterface orderService;

    public OrderController(OrderInterface orderService, JmsTemplate jmsTemplate) {
        this.orderService = orderService;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@Validated @RequestBody OrderDTO orderDto) {
    	jmsTemplate.convertAndSend(Constants.ORDER_CREATE_QUEUE, orderDto, message -> {
    	    message.setStringProperty("_type", "com.product.managemet.dto.OrderDTO");
    	    return message;
    	});
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_CREATE_ORDER_RESPONSE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @Validated @RequestBody OrderDTO orderDto) {
        OrderUpdateMessage orderUpdateMessage = new OrderUpdateMessage(id, orderDto);
        jmsTemplate.convertAndSend(Constants.ORDER_UPDATE_QUEUE, orderUpdateMessage, message -> {
    	    message.setStringProperty("_type", "com.product.managemet.dto.OrderUpdateMessage");
    	    return message;
    	});
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_UPDATE_ORDER_RESPONSE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        jmsTemplate.convertAndSend(Constants.ORDER_DELETE_QUEUE, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(Constants.REST_END_POINT_DELETE_ORDER_RESPONSE);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
