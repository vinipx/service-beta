package com.ecosystem.beta.controller;

import com.ecosystem.beta.dto.OrderDto;
import com.ecosystem.beta.service.OrderService;
import com.ecosystem.common.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderDto sampleOrderDto() {
        return new OrderDto(1L, 1L, "Laptop", 2, new BigDecimal("1500.00"), "PENDING",
                Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));
    }

    @Test
    void shouldReturnAllOrders_whenGetAllOrdersCalled() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(sampleOrderDto()));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].productName").value("Laptop"));
    }

    @Test
    void shouldReturnOrder_whenGetOrderByIdCalledWithValidId() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(sampleOrderDto());

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.productName").value("Laptop"));
    }

    @Test
    void shouldReturn404_whenGetOrderByIdCalledWithInvalidId() throws Exception {
        when(orderService.getOrderById(99L)).thenThrow(new NotFoundException("Order not found with id: 99"));

        mockMvc.perform(get("/api/v1/orders/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldCreateOrder_whenCreateOrderCalledWithValidData() throws Exception {
        OrderDto input = new OrderDto(null, 1L, "Laptop", 2, new BigDecimal("1500.00"), "PENDING", null, null);
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(sampleOrderDto());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void shouldUpdateOrder_whenUpdateOrderCalledWithValidData() throws Exception {
        OrderDto input = new OrderDto(null, 1L, "Laptop Pro", 3, new BigDecimal("2000.00"), "CONFIRMED", null, null);
        OrderDto updated = new OrderDto(1L, 1L, "Laptop Pro", 3, new BigDecimal("2000.00"), "CONFIRMED",
                Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        when(orderService.updateOrder(eq(1L), any(OrderDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productName").value("Laptop Pro"));
    }

    @Test
    void shouldReturn204_whenDeleteOrderCalledWithValidId() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_whenDeleteOrderCalledWithInvalidId() throws Exception {
        doThrow(new NotFoundException("Order not found with id: 99")).when(orderService).deleteOrder(99L);

        mockMvc.perform(delete("/api/v1/orders/99"))
                .andExpect(status().isNotFound());
    }
}
