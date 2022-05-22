package com.personal.quantization.center.service;

import java.util.List;

import com.personal.quantization.model.CenterTest;

public interface CenterTestService {
	
	public void saveCenterTest(CenterTest centerTest);

	public List<CenterTest> selectCenterTests();

}
