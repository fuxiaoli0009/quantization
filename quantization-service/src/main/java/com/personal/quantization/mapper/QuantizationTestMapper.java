package com.personal.quantization.mapper;

import java.util.List;

import com.personal.quantization.model.QuantizationTest;

public interface QuantizationTestMapper {

	void saveQuantizationTest(QuantizationTest test);
	
	List<QuantizationTest> selectQuantizationTests();

}
