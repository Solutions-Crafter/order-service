package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.OrderResponseDTO;
import com.ecommerce.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderRequestDTO requestDTO;
    private OrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new OrderRequestDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setProductId(2L);
        requestDTO.setQuantity(2);

        responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(1L);
        responseDTO.setCustomerId(1L);
        responseDTO.setProductName("Nike Shoes");
        responseDTO.setQuantity(2);
        responseDTO.setTotalPrice(10000.0);
        responseDTO.setStatus("PLACED");
    }

    @Test
    void createOrder_ShouldReturn201() {
        when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<OrderResponseDTO> response = orderController.createOrder(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nike Shoes", response.getBody().getProductName());
        assertEquals(10000.0, response.getBody().getTotalPrice());
    }
}