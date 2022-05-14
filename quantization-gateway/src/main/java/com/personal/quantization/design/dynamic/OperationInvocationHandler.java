package com.personal.quantization.design.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class OperationInvocationHandler<T> implements InvocationHandler {

	private T t;
	
	public OperationInvocationHandler(T t) {
		super();
		this.t = t;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("方法执行前增强内容");
		method.invoke(t, args);
		System.out.println("方法执行后增强内容");
		return null;
	}

}
