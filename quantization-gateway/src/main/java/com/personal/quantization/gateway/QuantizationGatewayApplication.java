package com.personal.quantization.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QuantizationGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuantizationGatewayApplication.class, args);
	}
}
