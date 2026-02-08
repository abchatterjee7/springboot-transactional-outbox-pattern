package org.aadi.common.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aadi.entity.Order;
import org.aadi.entity.Outbox;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderEntityToOutboxEntityMapper {


    @SneakyThrows
    public Outbox map(Order order) {
        // Create JSON manually with correct field names
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode orderEventJson = mapper.createObjectNode();
        orderEventJson.put("id", order.getId());
        orderEventJson.put("name", order.getName());
        orderEventJson.put("customerId", order.getCustomerId());
        orderEventJson.put("productType", order.getProductType());
        orderEventJson.put("quantity", order.getQuantity());
        orderEventJson.put("price", order.getPrice());
        orderEventJson.put("createdAt", order.getOrderDate() != null ? order.getOrderDate().toString() : new Date().toString());
        
        return Outbox.builder()
                .aggregateId(order.getId().toString())
                .payload(mapper.writeValueAsString(orderEventJson))
                .createdAt(new Date())
                .processed(false)
                .build();
    }
}
