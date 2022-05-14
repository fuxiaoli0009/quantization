package com.personal.quantization.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.personal.quantization.service.QuantizationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuantizationInvocationHandler implements InvocationHandler {

	@Autowired
	private QuantizationService quantizationServiceImpl; 
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start = System.currentTimeMillis();
		Object object = method.invoke(quantizationServiceImpl, args);
		log.info("【增强方法】{} 执行时间：{}", method.getName(), System.currentTimeMillis() - start);
		return object;
	}

}
