package com.personal.quantization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.personal.quantization.mapper")
@EnableScheduling
public class QuantizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantizationApplication.class, args);
	}
}
