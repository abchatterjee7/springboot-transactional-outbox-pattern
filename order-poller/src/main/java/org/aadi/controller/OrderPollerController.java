package org.aadi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aadi.entity.Outbox;
import org.aadi.service.OrderPollerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poller")
@Tag(name = "Order Poller Management", description = "APIs for managing order poller and outbox messages")
public class OrderPollerController {

    @Autowired
    private OrderPollerService orderPollerService;

    @GetMapping("/outbox")
    @Operation(summary = "Get unprocessed outbox messages", description = "Retrieves all unprocessed messages from the outbox table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unprocessed messages retrieved successfully")
    })
    public ResponseEntity<List<Outbox>> getUnprocessedMessages() {
        List<Outbox> messages = orderPollerService.getUnprocessedMessages();
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/trigger")
    @Operation(summary = "Trigger manual poll", description = "Manually triggers the outbox polling process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll triggered successfully"),
            @ApiResponse(responseCode = "500", description = "Error during polling")
    })
    public ResponseEntity<String> triggerPoll() {
        try {
            orderPollerService.pollOutboxMessagesAndPublish();
            return ResponseEntity.ok("Poll triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error during polling: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get poller status", description = "Returns the current status of the order poller service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    })
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Order Poller Service is running. Scheduled polling every 60 seconds.");
    }
}
