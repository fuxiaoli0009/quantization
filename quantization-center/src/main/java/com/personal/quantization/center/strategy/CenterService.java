package com.personal.quantization.center.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.personal.quantization.model.QuantizationHistoryDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CenterService extends AbstractCenterServiceStrategy {
	
	//https://q.stock.sohu.com/hisHq?code=cn_601766,cn_000002&start=20210101&end=20210312
	private static final String REMOTE_URL_SOHU = "https://q.stock.sohu.com/hisHq?code=";
	
	private static final String BETWEEN_TIME = "&start=20190101&end=20301230";
	
	@Autowired(required=true)
	RestTemplate restTemplate;
	
	@Override
	public String getRealTimeDatas(String quantizationCodes, String url) {
		return restTemplate.getForObject(url + quantizationCodes, String.class);
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
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		List<QuantizationHistoryDetail> historys = new ArrayList<>();
		QuantizationHistoryDetail history = new QuantizationHistoryDetail();
		history.setCode("");
		history.setStatus("");
		List<String> list = new ArrayList<>();
		list.add("0");
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");
		list.add("10");
		List<List<String>> hq = new ArrayList<>();
		hq.add(list);
		history.setHq(hq);
		historys.add(history);
		String jsonString = JSON.toJSONString(historys);
		System.out.println(JSON.toJSONString(historys));
		
		List<QuantizationHistoryDetail> result = JSONObject.parseArray(jsonString, QuantizationHistoryDetail.class);
		System.out.println(JSON.toJSONString(result));

	}


}
