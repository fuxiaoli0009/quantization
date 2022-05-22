package com.personal.quantization.center.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.quantization.center.mapper.CenterTestMapper;
import com.personal.quantization.center.service.CenterTestService;
import com.personal.quantization.model.CenterTest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CenterTestServiceImpl implements CenterTestService {
	
	@Autowired
	private CenterTestMapper centerTestMapper;

	@Override
	public void saveCenterTest(CenterTest centerTest) {
		centerTestMapper.saveCenterTest(centerTest);
	}

	@Override
	public List<CenterTest> selectCenterTests() {
		return centerTestMapper.selectCenterTests();
	}
    
	
}
