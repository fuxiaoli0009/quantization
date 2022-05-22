package com.personal.quantization.center.mapper;

import java.util.List;

import com.personal.quantization.model.CenterTest;

public interface CenterTestMapper {

	void saveCenterTest(CenterTest test);

	List<CenterTest> selectCenterTests();

}
