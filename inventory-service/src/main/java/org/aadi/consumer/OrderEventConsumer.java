package org.aadi.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aadi.dto.OrderEventDTO;
import org.aadi.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = "unprocessed-order-events", groupId = "inventory-service-group")
    public void consumeOrderEvent(String payload) {
        log.info("Received order event: {}", payload);
        
        try {
            OrderEventDTO orderEvent = objectMapper.readValue(payload, OrderEventDTO.class);
            log.info("Processed order event for product: {}, quantity: {}", 
                    orderEvent.getProductName(), orderEvent.getQuantity());
            
            // Update inventory based on the order
            inventoryService.updateInventoryForOrder(orderEvent);
            
        } catch (JsonProcessingException e) {
            log.error("Error processing order event: {}", e.getMessage());
            // In a real application, you might want to implement a dead letter queue
        } catch (Exception e) {
            log.error("Unexpected error processing order event: {}", e.getMessage());
        }
    }
}
