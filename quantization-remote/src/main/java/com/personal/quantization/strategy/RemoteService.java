package com.personal.quantization.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public abstract class RemoteService extends AbstractRemoteServiceStrategy {
	
	@Autowired(required=true)
	RestTemplate restTemplate;
	
	@Override
	public String getRealTimeDatas(String quantizationCodes, String url) {
		return restTemplate.getForObject(url + quantizationCodes, String.class);
	}


}
