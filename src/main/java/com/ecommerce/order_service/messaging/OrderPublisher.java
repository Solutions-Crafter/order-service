package com.ecommerce.order_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrder(OrderEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, event);
    }
}