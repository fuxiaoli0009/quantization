package com.personal.quantization.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.personal.quantization.model.QuantizationRealtimeInfo;
import com.personal.quantization.service.QuantizationChooseService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/quantization/choose")
@Slf4j
public class QuantizationChooseController {
	
	@ApiOperation(value = "1、定时全量更新pbpe", httpMethod = "GET")
    @RequestMapping(value = "/updatePBPE", method = RequestMethod.GET)
    public Integer updatePBPE(){
		return quantizationChooseService.updatePBPE();
	}
	
	@Autowired
	private QuantizationChooseService quantizationChooseService;
	
	@ApiOperation(value = "2、重新从选出待选中的", httpMethod = "GET")
    @RequestMapping(value = "/selectQuantizations", method = RequestMethod.GET)
    public String selectQuantizations(){
		//return quantizationChooseService.selectQuantizations();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("result", quantizationChooseService.selectQuantizations());
		return JSON.toJSONString(maps);
	}
	
	@ApiOperation(value = "3、按分组查询已选中的、待选中的和已买入的", httpMethod = "GET")
    @RequestMapping(value = "/selectQuantizationsByClassify", method = RequestMethod.GET)
	//@PreAuthorize("hasAuthority('product')")
	public String selectQuantizationsByClassify(){
		//return quantizationChooseService.selectQuantizationsByClassify();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("result", quantizationChooseService.selectQuantizationsByClassify());
		return JSON.toJSONString(maps);
	}
	
	@ApiOperation(value = "4、查询已选中的", httpMethod = "GET")
    @RequestMapping(value = "/selectQuantizationsBySelected", method = RequestMethod.GET)
    public String selectQuantizationsBySelected(){
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("result", quantizationChooseService.selectQuantizationsBySelected());
		return JSON.toJSONString(maps);
	}
	
	@ApiOperation(value = "更新状态", httpMethod = "GET")
    @RequestMapping(value = "/updateSelectedStatus", method = RequestMethod.GET)
    public void updateSelectedStatus(@RequestParam("quantizationCode") String quantizationCode,
    		@RequestParam("selectedStatus") String selectedStatus) {
		log.info("代码：{}，更新状态为：{}", quantizationCode, selectedStatus);
		quantizationChooseService.updateSelectedStatus(quantizationCode, selectedStatus);
    }
	
	@ApiOperation(value = "新增选中", httpMethod = "GET")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void add(QuantizationRealtimeInfo quantizationRealtimeInfo) {
		log.info("add quantization:{}", JSON.toJSONString(quantizationRealtimeInfo));
		quantizationChooseService.add(quantizationRealtimeInfo);
    }
}
