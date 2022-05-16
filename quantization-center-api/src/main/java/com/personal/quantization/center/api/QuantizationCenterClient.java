package com.personal.quantization.center.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationHistoryDetail;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.model.CenterQuantization;

@FeignClient(value="quantization-center", path="/center")
public interface QuantizationCenterClient {
	
	@RequestMapping(method=RequestMethod.POST, value="/obtainRealTimeDatas")
	Map<String, CenterQuantization> obtainRealTimeDatas(String quantizationCodes);
	
	@RequestMapping(method=RequestMethod.POST, value="/getQuantizationDetails")
	List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources);
	
	@RequestMapping(method=RequestMethod.POST, value="/getQuantizationHistoryDetails")
	List<QuantizationHistoryDetail> getQuantizationHistoryDetails(List<String> quantizationCodes);
	
}
