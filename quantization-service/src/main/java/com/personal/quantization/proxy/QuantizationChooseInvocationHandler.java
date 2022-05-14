package com.personal.quantization.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.personal.quantization.service.QuantizationChooseService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuantizationChooseInvocationHandler implements InvocationHandler {

	@Autowired
	private QuantizationChooseService quantizationChooseServiceImpl; 
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start = System.currentTimeMillis();
		Object object = method.invoke(quantizationChooseServiceImpl, args);
		log.info("【增强方法】{} 执行时间：{}", method.getName(), System.currentTimeMillis() - start);
		return object;
	}

}
