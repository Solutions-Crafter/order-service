package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.OrderResponseDTO;
import com.ecommerce.order_service.dto.ProductResponseDTO;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.messaging.OrderPublisher;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderPublisher orderPublisher;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDTO requestDTO;
    private ProductResponseDTO productResponse;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        requestDTO = new OrderRequestDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setProductId(2L);
        requestDTO.setQuantity(2);

        productResponse = new ProductResponseDTO();
        productResponse.setProductId(2L);
        productResponse.setName("Nike Shoes");
        productResponse.setUnitPrice(5000.0);

        savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setCustomerId(1L);
        savedOrder.setProductId(2L);
        savedOrder.setProductName("Nike Shoes");
        savedOrder.setQuantity(2);
        savedOrder.setTotalPrice(10000.0);
        savedOrder.setStatus("PLACED");
    }

    @Test
    void createOrder_ShouldReturnOrderResponse() {
        when(restTemplate.getForObject(
                eq("http://localhost:8081/api/products/2"),
                eq(ProductResponseDTO.class)
        )).thenReturn(productResponse);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        doNothing().when(orderPublisher).publishOrder(any());

        OrderResponseDTO response = orderService.createOrder(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("Nike Shoes", response.getProductName());
        assertEquals(10000.0, response.getTotalPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderPublisher, times(1)).publishOrder(any());
    }
}