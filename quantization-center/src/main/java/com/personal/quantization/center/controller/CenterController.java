package com.personal.quantization.center.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.center.constants.Constants;
import com.personal.quantization.center.context.CenterContext;
import com.personal.quantization.center.strategy.CenterService;
import com.personal.quantization.model.CenterQuantization;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationHistoryDetail;
import com.personal.quantization.model.QuantizationSource;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/center")
@Slf4j
public class CenterController {
	
	private CenterService centerService;
	
	@Autowired
	private CenterContext context;
	
	@Autowired
    public void Test(Map<String, CenterService> centerServiceMap) {
		log.info("for test.");
        this.centerService = centerServiceMap.get(context.REMOVE_SERVICE);
    }
	
    @RequestMapping(value = "/getQuantizationDetails", method = RequestMethod.POST)
    public List<QuantizationDetailInfo> getQuantizationDetails(@RequestBody List<QuantizationSource> quantizationSources) {
		return centerService.getQuantizationDetails(quantizationSources);
	}
    
    @RequestMapping(value = "/transferToMap", method = RequestMethod.POST)
    public Map<String, CenterQuantization> transferToMap(@RequestBody String result) {
    	return centerService.transferToMap(result);
	}
    
    @RequestMapping(value = "/getRealTimeDatas", method = RequestMethod.POST)
    public String getRealTimeDatas(@RequestBody String quantizationCodes) {
    	return centerService.getRealTimeDatas(quantizationCodes);
	}
    
    @RequestMapping(value = "/getQuantizationHistoryDetails", method = RequestMethod.POST)
    public List<QuantizationHistoryDetail> getQuantizationHistoryDetails(@RequestBody List<String> quantizationCodes) {
		return centerService.getQuantizationHistoryDetails(quantizationCodes);
	}
    
    
	public void switchSource() {
		if(Constants.REMOTE_SERVICE_TENCENT.equals(context.REMOVE_SERVICE)) {
			context.REMOVE_SERVICE = Constants.REMOTE_SERVICE_SINA;
		}else {
			context.REMOVE_SERVICE = Constants.REMOTE_SERVICE_TENCENT;
		}
	}
	
}
