package org.aadi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.aadi.dto.OrderEventDTO;
import org.aadi.entity.Inventory;
import org.aadi.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
@Tag(name = "Inventory Management", description = "APIs for managing inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productName}")
    @Operation(summary = "Get inventory by product name", description = "Retrieves inventory information for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory found"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<Inventory> getInventoryByProductName(@PathVariable String productName) {
        Optional<Inventory> inventory = inventoryService.getInventoryByProductName(productName);
        return inventory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all inventory", description = "Retrieves all inventory items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully")
    })
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/add")
    @Operation(summary = "Add inventory", description = "Adds new inventory or updates existing inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory added successfully"),
            @ApiResponse(responseCode = "500", description = "Error adding inventory")
    })
    public ResponseEntity<Inventory> addInventory(@RequestBody AddInventoryRequest request) {
        try {
            Inventory inventory = inventoryService.addInventory(
                    request.getProductName(),
                    request.getProductType(),
                    request.getQuantity(),
                    request.getPrice()
            );
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            log.error("Error adding inventory: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update-for-order")
    @Operation(summary = "Update inventory for order", description = "Updates inventory based on order event (usually called by Kafka consumer)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
            @ApiResponse(responseCode = "500", description = "Error updating inventory")
    })
    public ResponseEntity<String> updateInventoryForOrder(@RequestBody OrderEventDTO orderEvent) {
        try {
            inventoryService.updateInventoryForOrder(orderEvent);
            return ResponseEntity.ok("Inventory updated successfully");
        } catch (Exception e) {
            log.error("Error updating inventory for order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update inventory: " + e.getMessage());
        }
    }

    // DTO for add inventory request
    public static class AddInventoryRequest {
        private String productName;
        private String productType;
        private int quantity;
        private BigDecimal price;

        // Getters and setters
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getProductType() { return productType; }
        public void setProductType(String productType) { this.productType = productType; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
