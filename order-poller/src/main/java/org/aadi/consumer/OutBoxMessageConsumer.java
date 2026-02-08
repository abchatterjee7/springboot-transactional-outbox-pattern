package org.aadi.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OutBoxMessageConsumer {
    // This class can be used for logging or other utility functions
    // Order poller should only read from outbox table and publish to Kafka
    // It should NOT consume from Kafka - that's the inventory service's job
    
    public void logPollerActivity(String message) {
        log.info("Order Poller Activity: {}", message);
    }
}
