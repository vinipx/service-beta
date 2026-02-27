package com.ecosystem.beta.service;

import com.ecosystem.beta.client.UserServiceClient;
import com.ecosystem.beta.dto.OrderDto;
import com.ecosystem.beta.model.Order;
import com.ecosystem.beta.repository.OrderRepository;
import com.ecosystem.common.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    public OrderServiceImpl(OrderRepository orderRepository, UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        userServiceClient.getUserById(orderDto.userId());
        Order order = toEntity(orderDto);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        return toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
        existing.setUserId(orderDto.userId());
        existing.setProductName(orderDto.productName());
        existing.setQuantity(orderDto.quantity());
        existing.setTotalPrice(orderDto.totalPrice());
        existing.setStatus(orderDto.status());
        existing.setUpdatedAt(Instant.now());
        return toDto(orderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getProductName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setUserId(dto.userId());
        order.setProductName(dto.productName());
        order.setQuantity(dto.quantity());
        order.setTotalPrice(dto.totalPrice());
        order.setStatus(dto.status());
        return order;
    }
}
