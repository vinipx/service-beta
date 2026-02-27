package com.ecosystem.beta.repository;

import com.ecosystem.beta.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order createOrder(String productName, Long userId, String status) {
        Order order = new Order();
        order.setUserId(userId);
        order.setProductName(productName);
        order.setQuantity(1);
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setStatus(status);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        return order;
    }

    @Test
    void shouldSaveOrder_whenValidOrderProvided() {
        Order order = createOrder("Laptop", 1L, "PENDING");

        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getProductName()).isEqualTo("Laptop");
    }

    @Test
    void shouldFindOrderById_whenOrderExists() {
        Order saved = orderRepository.save(createOrder("Phone", 1L, "PENDING"));

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getProductName()).isEqualTo("Phone");
    }

    @Test
    void shouldReturnEmpty_whenOrderNotFound() {
        Optional<Order> found = orderRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnAllOrders_whenFindAllCalled() {
        orderRepository.save(createOrder("Laptop", 1L, "PENDING"));
        orderRepository.save(createOrder("Phone", 2L, "CONFIRMED"));

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldDeleteOrder_whenDeleteByIdCalled() {
        Order saved = orderRepository.save(createOrder("Tablet", 1L, "PENDING"));
        Long id = saved.getId();

        orderRepository.deleteById(id);

        assertThat(orderRepository.findById(id)).isEmpty();
    }

    @Test
    void shouldReturnTrue_whenOrderExistsById() {
        Order saved = orderRepository.save(createOrder("Headphones", 1L, "PENDING"));

        assertThat(orderRepository.existsById(saved.getId())).isTrue();
    }

    @Test
    void shouldReturnFalse_whenOrderDoesNotExistById() {
        assertThat(orderRepository.existsById(999L)).isFalse();
    }
}
