package com.product.managemet.serviceimp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.managemet.dto.OrderDTO;
import com.product.managemet.entity.Order;
import com.product.managemet.entity.Product;
import com.product.managemet.exception.ResourceNotFoundException;
import com.product.managemet.repository.OrderRepository;
import com.product.managemet.repository.ProductRepository;
import com.product.managemet.service.OrderInterface;
import com.product.managemet.util.Constants;

@Service
public class OrderService implements OrderInterface {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        Set<Product> products = orderDto.getProductIds().stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + productId)))
                .collect(Collectors.toSet());

        order.setProducts(products);
        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.ORDER_NOT_FOUND + id));

        order.setOrderDate(LocalDateTime.now());

        if (orderDto.getProductIds() != null && !orderDto.getProductIds().isEmpty()) {
            Set<Product> products = orderDto.getProductIds().stream()
                    .map(productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + productId)))
                    .collect(Collectors.toSet());
            order.setProducts(products);
        }

        Order updatedOrder = orderRepository.save(order);
        return mapToDto(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.ORDER_NOT_FOUND + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.ORDER_NOT_FOUND + id));
        return mapToDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private OrderDTO mapToDto(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        Set<Long> productIds = order.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        dto.setProductIds(productIds);
        return dto;
    }
}
