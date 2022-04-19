package com.personal.quantization.config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;

@Component
public class MongoDBConfig {
	/*
	 * private final static Logger logger =
	 * LoggerFactory.getLogger(MongoDBConfig.class);
	 * 
	 * @Autowired private MongoDBProperties mongoDBProperties;
	 * 
	 * private MongoClient mongoClient;
	 * 
	 * @Bean //覆盖
	 * 
	 * @ConditionalOnMissingBean(MongoClient.class) public MongoClient
	 * getMongodbClients() { String host = mongoDBProperties.getHost();
	 * if(StringUtils.isEmpty(host)){ throw new
	 * RuntimeException("MongoDB host is empty."); } String[] hosts =
	 * host.split(","); String[] address; List<ServerAddress> addresses = new
	 * ArrayList<ServerAddress>(); for( String h : hosts ){ if(
	 * StringUtils.isEmpty(h) ){
	 * logger.warn("This MongoDB host [{}] is empty, continue init this host.", h);
	 * continue; }
	 * 
	 * if( h.indexOf(":") != -1 ){ address = h.split(":");
	 * logger.info("Init MongoDB : host [{}], port [{}]", address[0], address[1]);
	 * addresses.add(new ServerAddress(address[0], Integer.valueOf(address[1])));
	 * }else{ logger.info("Init MongoDB : host [{}], no port.", h);
	 * addresses.add(new ServerAddress(h)); } } MongoCredential credential =
	 * MongoCredential.createCredential(mongoDBProperties.getUsername(),
	 * mongoDBProperties.getDatabase(),
	 * mongoDBProperties.getPassword().toCharArray()); mongoClient = new
	 * MongoClients(addresses, Arrays.asList(credential)); }
	 */
}
