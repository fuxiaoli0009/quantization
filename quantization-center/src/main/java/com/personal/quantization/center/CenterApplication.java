package com.personal.quantization.center;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.personal.quantization.center.mapper")
@SpringBootApplication
public class CenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CenterApplication.class, args);
	}
}
