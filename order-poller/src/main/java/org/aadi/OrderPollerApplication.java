package org.aadi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrderPollerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderPollerApplication.class, args);
	}

}
