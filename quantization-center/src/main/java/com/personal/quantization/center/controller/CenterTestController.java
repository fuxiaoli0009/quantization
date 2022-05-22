package com.personal.quantization.center.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.center.service.CenterTestService;
import com.personal.quantization.center.service.RocketMQService;
import com.personal.quantization.model.CenterTest;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/center")
@Slf4j
public class CenterTestController {
	
	@Autowired
	private RocketMQService rocketMQService;
	
	@Autowired
	private CenterTestService centerTestService;
	
	@ApiOperation(value = "saveCenterTest", httpMethod = "POST")
    @RequestMapping(value = "/saveCenterTest", method = RequestMethod.POST)
    public void saveCenterTest(@RequestBody CenterTest centerTest){
		centerTestService.saveCenterTest(centerTest);
	}
	
	@RequestMapping(value = "/selectCenterTests", method = RequestMethod.POST)
    public List<CenterTest> selectCenterTests() {
		return centerTestService.selectCenterTests();
	}
	
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void sendMessage() {
    	for(int i=1; i<10; i++) {
    		try {
				rocketMQService.pushMessage(String.valueOf(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}
    
    @RequestMapping(value = "/sendMessageOrderly", method = RequestMethod.POST)
    public void sendMessageOrderly() {
		rocketMQService.sendMessageOrderly();
	}
    
    
    
    
}
