package com.personal.quantization.manager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

//@Component
@Data
//@ConfigurationProperties(prefix="spring.datasource.auth")
public class AuthDBConfig {

	private String jdbcUrl;
    private String username;
    private String password;
    private int minPoolSize;
    private int maxPoolSize;
    private int maxLifetime;
    private int borrowConnectionTimeout;
    private int loginTimeout;
    private int maintenanceInterval;
}
