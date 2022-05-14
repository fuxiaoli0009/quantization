package com.personal.quantization.center.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.personal.quantization.center.Interceptor.TimeInterceptor;
import com.personal.quantization.center.strategy.impl.TencentService;

import net.sf.cglib.proxy.Enhancer;

@Configuration
public class EnchancerConfig {

	@Autowired
	private TimeInterceptor timeInterceptor;
	
	@Bean
	public TencentService tencentServiceProxy() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(TencentService.class);
		enhancer.setCallback(timeInterceptor);
		TencentService tencentServiceProxy = (TencentService)enhancer.create();
		return tencentServiceProxy;
	}
}
