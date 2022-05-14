package com.personal.quantization;

import java.lang.reflect.Proxy;

import com.personal.quantization.design.dynamic.QuantizationInvocationHandler;
import com.personal.quantization.design.dynamic.QuantizationMethod;
import com.personal.quantization.design.dynamic.QuantizationService;
import com.personal.quantization.design.dynamic.QuantizationServiceImpl;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class Main {

	public static void main(String[] args) {
		
		//jdk dynamic proxy method
		QuantizationService quantizationService = new QuantizationServiceImpl();
		QuantizationInvocationHandler handler = new QuantizationInvocationHandler(quantizationService);
		QuantizationService proxyService = (QuantizationService)Proxy.newProxyInstance(quantizationService.getClass().getClassLoader(), quantizationService.getClass().getInterfaces(), handler);
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		String result = proxyService.operate();
		System.out.println("main result : " + result);
		//cglib dynamic proxy method
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\class");  
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(new TimeInterceptor());
		enhancer.setSuperclass(QuantizationMethod.class);
		QuantizationMethod proxy = (QuantizationMethod)enhancer.create();
		System.out.println(proxy.operate());
	}
}
