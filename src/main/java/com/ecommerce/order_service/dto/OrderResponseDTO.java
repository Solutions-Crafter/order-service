package com.ecommerce.order_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {

    private Long orderId;
    private Long customerId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double totalPrice;
    private String status;
    private LocalDateTime orderDate;
}