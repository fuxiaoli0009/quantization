package com.personal.quantization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.service.QuantizationService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/quantization")
@Slf4j
public class QuantizationController {
	
	@Autowired
	private QuantizationService quantizationService;
	
	@ApiOperation(value = "实时获取已选中的SOURCE数据", httpMethod = "GET")
    @RequestMapping(value = "/selectQuantizationsBySource", method = RequestMethod.GET)
    public String selectQuantizationsBySource(String sources){
		return quantizationService.selectQuantizationsBySource(sources);
	}
	
	@ApiOperation(value = "实时获取已选中的SOURCE=A和B的数据", httpMethod = "GET")
    @RequestMapping(value = "/queryQuantizationsBySelectedStatus", method = RequestMethod.GET)
    public String queryQuantizationsBySelectedStatus(){
		return quantizationService.queryQuantizationsBySelectedStatus();
	}
	
	@ApiOperation(value = "更新", httpMethod = "GET")
    @RequestMapping(value = "/updateQuantization", method = RequestMethod.GET)
    public void update(@RequestParam("column") String column, @RequestParam("quantizationCode") String quantizationCode, @RequestParam("value") String value) {
    	log.info("column:{}, code:{}, value:{}", column, quantizationCode, value);
    	quantizationService.updateQuantization(column, quantizationCode, value);
    }
	
	@ApiOperation(value = "获取历史数据【该接口需要改为定时任务，定时获取历史数据】", httpMethod = "GET")
    @RequestMapping(value = "/getQuantizationHistoryDetails", method = RequestMethod.GET)
    public void getQuantizationHistoryDetails() {
    	quantizationService.getQuantizationHistoryDetails();
    }
	
	@ApiOperation(value = "计算真实值，并保存mongo", httpMethod = "GET")
    @RequestMapping(value = "/calculateIndex", method = RequestMethod.GET)
    public String calculateIndex() {
    	return quantizationService.calculateIndex();
    }
	
	@ApiOperation(value = "从mongo查询真实值", httpMethod = "GET")
    @RequestMapping(value = "/getIndex", method = RequestMethod.GET)
    public String getIndex() {
    	return quantizationService.getIndex();
    }
	
	
}
