package com.personal.quantization.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("spring.data.mongodb")
@Data
public class MongoDBProperties {

    private String host;
    
    private String username;
    
    private String password;
    
    private String database;

}

