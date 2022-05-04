package com.personal.quantization.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.mock.vo.QuantizationResult;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/mock")
public class QuantizationMockController {

	@Value("${remote.url}")
	private String REMOTE_URL;

	@Autowired
	private OAuth2RestTemplate oAuth2RestTemplate;

    @RequestMapping(value = "/getIndex", method = RequestMethod.GET)
    public String getIndex() {
    	ResponseEntity<String> entity = oAuth2RestTemplate
				.getForEntity(REMOTE_URL + "/quantization/getIndex", String.class);
		String body = entity.getBody();
		System.out.println("getIndex body: " + body);
		return body;
    }
    
    @RequestMapping(value = "/queryQuantizationsBySelectedStatus", method = RequestMethod.GET)
    public String queryQuantizationsBySelectedStatus(){
    	ResponseEntity<String> entity = oAuth2RestTemplate
				.getForEntity(REMOTE_URL + "/quantization/queryQuantizationsBySelectedStatus", String.class);
		String body = entity.getBody();
		System.out.println("queryQuantizationsBySelectedStatus body: " + body);
		return body;
	}

	@GetMapping("/choose/selectQuantizations")
	public String selectQuantizations() {

		ResponseEntity<String> entity = oAuth2RestTemplate
				.getForEntity(REMOTE_URL + "/quantization/choose/selectQuantizations", String.class);
		String body = entity.getBody();
		System.out.println("selectQuantizations body: " + body);
		return body;
	}

}
