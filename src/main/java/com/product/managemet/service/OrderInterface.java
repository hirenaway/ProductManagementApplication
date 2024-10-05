package com.product.managemet.service;

import java.util.List;

import com.product.managemet.dto.OrderDTO;

public interface OrderInterface {

	OrderDTO createOrder(OrderDTO orderDto);

	OrderDTO updateOrder(Long id, OrderDTO orderDto);

	void deleteOrder(Long id);

	OrderDTO getOrderById(Long id);

	List<OrderDTO> getAllOrders();

}
