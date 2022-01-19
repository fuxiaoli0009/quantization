package com.personal.quantization.knowledge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.personal.quantization.knowledge.mapper")
@EnableScheduling
public class QuantizationKnowledgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantizationKnowledgeApplication.class, args);
	}
}
