package com.personal.quantization.strategy;

import java.util.List;
import java.util.Map;

import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.model.RemoteQuantization;

public abstract class AbstractRemoteServiceStrategy {
	
	public abstract String getRealTimeDatas(String quantizationCodes);

	public abstract String getRealTimeDatas(String quantizationCodes, String url);
	
	public abstract Map<String, RemoteQuantization> transferToMap(String result);
	
	public abstract List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources);
	
	public abstract List<QuantizationDetailInfo> parseQuantizationDetails(String result);
}
