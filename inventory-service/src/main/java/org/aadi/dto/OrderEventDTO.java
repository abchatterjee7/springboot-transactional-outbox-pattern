package org.aadi.dto;

// Use the same DTO as order-service to ensure consistency
// This class should be identical to org.aadi.common.dto.OrderEventDTO
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventDTO {
    
    private Long id;
    
    @JsonProperty("name")
    private String productName;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("productType")
    private String productType;
    
    @JsonProperty("quantity")
    private Integer quantity;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("createdAt")
    private String createdAt;
}
