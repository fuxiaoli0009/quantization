package com.personal.quantization.center.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.model.CenterQuantization;

@FeignClient(value="quantization-center", path="/center")
public interface QuantizationCenterClient {
	
	@RequestMapping(method=RequestMethod.POST, value="/getQuantizationDetails")
	List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources);
	
	@RequestMapping(method=RequestMethod.POST, value="/transferToMap")
	Map<String, CenterQuantization> transferToMap(String result);
	
	@RequestMapping(method=RequestMethod.POST, value="/getRealTimeDatas")
	String getRealTimeDatas(String quantizationCodes);
}
