package com.personal.quantization.design.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class QuantizationInvocationHandler implements InvocationHandler {
	
	private QuantizationService quantizationService;

	
	public QuantizationInvocationHandler(QuantizationService quantizationService) {
		super();
		this.quantizationService = quantizationService;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		System.out.println("before");
		String result = (String)method.invoke(quantizationService, args);
		System.out.println("after");
		return result;
	}

}
