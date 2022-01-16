package com.personal.quantization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.personal.quantization.mapper")
@EnableScheduling
@EnableFeignClients(basePackages = "com.personal.quantization.center.api")
public class QuantizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantizationApplication.class, args);
	}
}
