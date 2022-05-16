package com.personal.quantization.center.strategy;

import java.util.List;
import java.util.Map;

import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.model.CenterQuantization;

public abstract class AbstractCenterServiceStrategy {
	
	public abstract String getServiceURL();

	public abstract String getRealTimeDatasFromRemote(String quantizationCodes, String url);
	
	public abstract Map<String, CenterQuantization> transferToMap(String result);
	
	public abstract List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources);
	
	public abstract List<QuantizationDetailInfo> parseQuantizationDetails(String result);
}
