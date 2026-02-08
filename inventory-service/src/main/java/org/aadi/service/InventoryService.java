package org.aadi.service;

import lombok.extern.slf4j.Slf4j;
import org.aadi.dto.OrderEventDTO;
import org.aadi.entity.Inventory;
import org.aadi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public Inventory updateInventoryForOrder(OrderEventDTO orderEvent) {
        log.info("Updating inventory for order: product={}, quantity={}", 
                orderEvent.getProductName(), orderEvent.getQuantity());

        try {
            // Find existing inventory item
            Optional<Inventory> existingInventory = inventoryRepository
                    .findByProductName(orderEvent.getProductName());

            Inventory inventory;
            if (existingInventory.isPresent()) {
                // Update existing inventory
                inventory = existingInventory.get();
                int currentStock = inventory.getStockQuantity();
                int orderQuantity = orderEvent.getQuantity();
                
                if (currentStock >= orderQuantity) {
                    inventory.setStockQuantity(currentStock - orderQuantity);
                    log.info("Reduced stock for {} from {} to {}", 
                            orderEvent.getProductName(), currentStock, inventory.getStockQuantity());
                } else {
                    log.warn("Insufficient stock for {}. Current: {}, Required: {}", 
                            orderEvent.getProductName(), currentStock, orderQuantity);
                    // In a real application, you might want to handle this differently
                    // For now, we'll set stock to 0 to indicate oversold
                    inventory.setStockQuantity(0);
                }
            } else {
                // Create new inventory item with negative stock (backorder scenario)
                inventory = Inventory.builder()
                        .productName(orderEvent.getProductName())
                        .productType(orderEvent.getProductType())
                        .stockQuantity(-orderEvent.getQuantity())
                        .price(orderEvent.getPrice())
                        .build();
                log.info("Created new inventory item for {} with backorder quantity: {}", 
                        orderEvent.getProductName(), -orderEvent.getQuantity());
            }

            return inventoryRepository.save(inventory);

        } catch (Exception e) {
            log.error("Error updating inventory for order event: {}", e.getMessage());
            throw new RuntimeException("Failed to update inventory", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Inventory> getInventoryByProductName(String productName) {
        return inventoryRepository.findByProductName(productName);
    }

    @Transactional
    public Inventory addInventory(String productName, String productType, int quantity, BigDecimal price) {
        Optional<Inventory> existing = inventoryRepository.findByProductName(productName);
        
        if (existing.isPresent()) {
            Inventory inventory = existing.get();
            inventory.setStockQuantity(inventory.getStockQuantity() + quantity);
            if (price != null) {
                inventory.setPrice(price);
            }
            return inventoryRepository.save(inventory);
        } else {
            Inventory newInventory = Inventory.builder()
                    .productName(productName)
                    .productType(productType)
                    .stockQuantity(quantity)
                    .price(price)
                    .build();
            return inventoryRepository.save(newInventory);
        }
    }
}
