package com.personal.quantization.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.center.service.RocketMQService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/center")
@Slf4j
public class TestController {
	
	@Autowired
	private RocketMQService rocketMQService;
	
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
    
    
    
    
}
