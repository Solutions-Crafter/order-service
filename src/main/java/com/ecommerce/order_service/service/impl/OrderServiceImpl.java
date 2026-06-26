package com.ecommerce.order_service.service.impl;

import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.OrderResponseDTO;
import com.ecommerce.order_service.dto.ProductResponseDTO;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.messaging.OrderEvent;
import com.ecommerce.order_service.messaging.OrderPublisher;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;
    private final RestTemplate restTemplate;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {

        // 1. Call Product Service to get product details
        String productUrl = "http://localhost:8081/api/products/" + requestDTO.getProductId();
        ProductResponseDTO product = restTemplate.getForObject(productUrl, ProductResponseDTO.class);

        // 2. Calculate total price
        double totalPrice = product.getUnitPrice() * requestDTO.getQuantity();

        // 3. Save order
        Order order = new Order();
        order.setCustomerId(requestDTO.getCustomerId());
        order.setProductId(requestDTO.getProductId());
        order.setProductName(product.getName());
        order.setQuantity(requestDTO.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        // 4. Publish to RabbitMQ
        OrderEvent event = new OrderEvent(
                saved.getOrderId(),
                saved.getCustomerId(),
                saved.getProductName(),
                saved.getQuantity(),
                saved.getTotalPrice()
        );
        orderPublisher.publishOrder(event);

        // 5. Return response
        return mapToResponseDTO(saved);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setCustomerId(order.getCustomerId());
        response.setProductId(order.getProductId());
        response.setProductName(order.getProductName());
        response.setQuantity(order.getQuantity());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        return response;
    }
}