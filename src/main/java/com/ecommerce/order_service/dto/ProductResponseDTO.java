package com.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private Double unitPrice;
    private String description;
    private String category;
    private Integer stock;
}