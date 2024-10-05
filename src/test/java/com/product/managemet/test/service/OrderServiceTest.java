package com.product.managemet.test.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.product.managemet.dto.OrderDTO;
import com.product.managemet.entity.Order;
import com.product.managemet.entity.Product;
import com.product.managemet.exception.ResourceNotFoundException;
import com.product.managemet.repository.OrderRepository;
import com.product.managemet.repository.ProductRepository;
import com.product.managemet.serviceimp.OrderService;
import com.product.managemet.util.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    private Order order;
    private OrderDTO orderDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Product A");

        order = new Order();
        order.setId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setProducts(Set.of(product));

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setProductIds(Set.of(1L));
    }

    @Test
    void testCreateOrder() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getId());
        assertEquals(1, createdOrder.getProductIds().size());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                orderService.createOrder(orderDTO));

        assertEquals(Constants.PRODUCT_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testUpdateOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO updatedOrder = orderService.updateOrder(1L, orderDTO);

        assertNotNull(updatedOrder);
        assertEquals(1L, updatedOrder.getId());
        assertEquals(1, updatedOrder.getProductIds().size());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                orderService.updateOrder(1L, orderDTO));

        assertEquals(Constants.ORDER_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testUpdateOrderProductNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                orderService.updateOrder(1L, orderDTO));

        assertEquals(Constants.PRODUCT_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                orderService.deleteOrder(1L));

        assertEquals(Constants.ORDER_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testDeleteOrderFailure() {
        when(orderRepository.existsById(anyLong())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(orderRepository).deleteById(anyLong());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.deleteOrder(1L));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        OrderDTO foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                orderService.getOrderById(1L));

        assertEquals(Constants.ORDER_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testGetOrderByIdFailure() {
        when(orderRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.getOrderById(1L));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<OrderDTO> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrdersFailure() {
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.getAllOrders());

        assertEquals("Database error", exception.getMessage());
    }
}

