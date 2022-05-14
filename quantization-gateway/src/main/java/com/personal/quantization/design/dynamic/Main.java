package com.personal.quantization.design.dynamic;

import java.lang.reflect.Proxy;

public class Main {

	public static void main(String[] args) {
		QuantizationOperation impl = new QuantizationOperationImpl();
		OperationInvocationHandler<QuantizationOperation> handler = new OperationInvocationHandler<>(impl);
		QuantizationOperation operation = (QuantizationOperation)Proxy.newProxyInstance(handler.getClass().getClassLoader(), impl.getClass().getInterfaces(), handler);
		operation.operate();
	}

}
