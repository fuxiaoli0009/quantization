package com.personal.quantization.design.dynamic;

public class QuantizationServiceImpl implements QuantizationService {

	@Override
	public String operate() {
		System.out.println("执行operate");
		return "operate";
	}

}
