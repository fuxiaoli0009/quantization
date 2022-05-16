package com.personal.quantization.design;

import java.lang.reflect.Proxy;

import com.personal.quantization.TimeInterceptor;
import com.personal.quantization.design.bridge.AbstractSquareOrRectangleBrick;
import com.personal.quantization.design.bridge.BigBrick;
import com.personal.quantization.design.bridge.BlueBrick;
import com.personal.quantization.design.bridge.SquareBrick;
import com.personal.quantization.design.dynamic.QuantizationInvocationHandler;
import com.personal.quantization.design.dynamic.QuantizationMethod;
import com.personal.quantization.design.dynamic.QuantizationService;
import com.personal.quantization.design.dynamic.QuantizationServiceImpl;
import com.personal.quantization.design.flyweight.Phone;
import com.personal.quantization.design.flyweight.PhoneFactory;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class Main {

	public static void main(String[] args) {
		
		bridge();
	}
	
	public static void bridge() {
		AbstractSquareOrRectangleBrick brick1 = new SquareBrick(new BlueBrick(), new BigBrick());
		brick1.produce();
	}
	
	/**
	 * flyWeight
	 */
	public static void flyWeight() {
		String[] branchs = {"apple", "xiaomi", "apple", "huawei", "sanxing", "huawei", "huawei", "apple"};
		for(String branch : branchs) {
			Phone phone = PhoneFactory.getPhone(branch);
			phone.setPrice(Math.random()*1000);
			System.out.println(phone.toString());
		}
	}
	
	/**
	 * dynamic
	 */
	public static void dynamic() {
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
