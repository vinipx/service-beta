package com.ecosystem.beta.service;

import com.ecosystem.beta.client.UserServiceClient;
import com.ecosystem.beta.dto.OrderDto;
import com.ecosystem.beta.model.Order;
import com.ecosystem.beta.repository.OrderRepository;
import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order sampleOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setProductName("Laptop");
        order.setQuantity(2);
        order.setTotalPrice(new BigDecimal("1500.00"));
        order.setStatus("PENDING");
        order.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        order.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        return order;
    }

    private UserDto sampleUserDto() {
        return new UserDto(1L, "john.doe", "john@example.com", "John Doe",
                Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));
    }

    @Test
    void shouldReturnAllOrders_whenGetAllOrdersCalled() {
        when(orderRepository.findAll()).thenReturn(List.of(sampleOrder()));

        List<OrderDto> result = orderService.getAllOrders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).productName()).isEqualTo("Laptop");
    }

    @Test
    void shouldReturnOrder_whenGetOrderByIdCalledWithValidId() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder()));

        OrderDto result = orderService.getOrderById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.productName()).isEqualTo("Laptop");
    }

    @Test
    void shouldThrowNotFoundException_whenGetOrderByIdCalledWithInvalidId() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldCreateOrder_whenCreateOrderCalledWithValidUserAndData() {
        OrderDto input = new OrderDto(null, 1L, "Laptop", 2, new BigDecimal("1500.00"), "PENDING", null, null);
        when(userServiceClient.getUserById(1L)).thenReturn(sampleUserDto());
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder());

        OrderDto result = orderService.createOrder(input);

        assertThat(result.productName()).isEqualTo("Laptop");
        verify(userServiceClient).getUserById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowNotFoundException_whenCreateOrderCalledWithInvalidUserId() {
        OrderDto input = new OrderDto(null, 99L, "Laptop", 2, new BigDecimal("1500.00"), "PENDING", null, null);
        when(userServiceClient.getUserById(99L)).thenThrow(new NotFoundException("User not found with id: 99"));

        assertThatThrownBy(() -> orderService.createOrder(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldUpdateOrder_whenUpdateOrderCalledWithValidId() {
        OrderDto input = new OrderDto(null, 1L, "Laptop Pro", 3, new BigDecimal("2000.00"), "CONFIRMED", null, null);
        Order existing = sampleOrder();
        Order updated = sampleOrder();
        updated.setProductName("Laptop Pro");
        updated.setQuantity(3);
        updated.setTotalPrice(new BigDecimal("2000.00"));
        updated.setStatus("CONFIRMED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(orderRepository.save(any(Order.class))).thenReturn(updated);

        OrderDto result = orderService.updateOrder(1L, input);

        assertThat(result.productName()).isEqualTo("Laptop Pro");
        assertThat(result.status()).isEqualTo("CONFIRMED");
    }

    @Test
    void shouldThrowNotFoundException_whenUpdateOrderCalledWithInvalidId() {
        OrderDto input = new OrderDto(null, 1L, "Laptop Pro", 3, new BigDecimal("2000.00"), "CONFIRMED", null, null);
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrder(99L, input))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteOrder_whenDeleteOrderCalledWithValidId() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteOrderCalledWithInvalidId() {
        when(orderRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> orderService.deleteOrder(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}