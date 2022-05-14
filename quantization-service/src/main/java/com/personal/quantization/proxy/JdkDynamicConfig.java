package com.personal.quantization.proxy;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.personal.quantization.service.QuantizationChooseService;
import com.personal.quantization.service.QuantizationService;

@Configuration
public class JdkDynamicConfig {

	@Autowired
	private QuantizationChooseInvocationHandler quantizationChooseInvocationHandler;
	
	@Autowired
	private QuantizationInvocationHandler quantizationInvocationHandler;
	
	@Autowired
	private QuantizationChooseService quantizationChooseServiceImpl;
	
	@Autowired
	private QuantizationService quantizationServiceImpl;
	
	@Bean
	public QuantizationChooseService quantizationChooseServiceProxy() {
		return (QuantizationChooseService)Proxy.newProxyInstance(quantizationChooseServiceImpl.getClass().getClassLoader(), quantizationChooseServiceImpl.getClass().getInterfaces(), quantizationChooseInvocationHandler);
	}
	
	@Bean
	public QuantizationService quantizationServiceProxy() {
		return (QuantizationService)Proxy.newProxyInstance(quantizationServiceImpl.getClass().getClassLoader(), quantizationServiceImpl.getClass().getInterfaces(), quantizationInvocationHandler);
	}
}
