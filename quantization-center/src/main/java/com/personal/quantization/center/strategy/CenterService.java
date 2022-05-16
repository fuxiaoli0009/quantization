package com.personal.quantization.center.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.personal.quantization.model.CenterQuantization;
import com.personal.quantization.model.QuantizationHistoryDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CenterService extends AbstractCenterServiceStrategy {
	
	//https://q.stock.sohu.com/hisHq?code=cn_601766,cn_000002&start=20210101&end=20210312
	private static final String REMOTE_URL_SOHU = "https://q.stock.sohu.com/hisHq?code=";
	
	private static final String BETWEEN_TIME = "&start=20190101&end=20301230";
	
	@Autowired(required=true)
	RestTemplate restTemplate;
	
	/**
	 * 模板方法
	 * @param quantizationCodes
	 * @param url
	 * @return
	 */
	public final Map<String, CenterQuantization> obtainRealTimeDatas(String quantizationCodes) {
		
		//抽象方法
		String serviceURL = this.getServiceURL();
		//具体方法
		String result = this.getRealTimeDatasFromRemote(quantizationCodes, serviceURL);
		//抽象方法
		return this.transferToMap(result);
	} 
	
	@Override
	public final String getRealTimeDatasFromRemote(String quantizationCodes, String url) {
		long start = System.currentTimeMillis();
		String result = restTemplate.getForObject(url + quantizationCodes, String.class);
		log.info("调用Tecent接口查询quantizationCodes耗时：{}，quantizationCodes: {}", System.currentTimeMillis() - start, quantizationCodes);
		return result;
	}

	public List<QuantizationHistoryDetail> getQuantizationHistoryDetails(List<String> quantizationCodes) {
		
		List<QuantizationHistoryDetail> result = new ArrayList<QuantizationHistoryDetail>();
		List<List<String>> splitCodes = Lists.partition(quantizationCodes, 5);
		splitCodes.stream().forEach(list -> {
			String jsonResult = restTemplate.getForObject(REMOTE_URL_SOHU + String.join(",", list) + BETWEEN_TIME, String.class);
			result.addAll(JSONObject.parseArray(jsonResult, QuantizationHistoryDetail.class));
			log.info("成功获取数据：{}", String.join(",", list));
		});
		return result;
	}
}
