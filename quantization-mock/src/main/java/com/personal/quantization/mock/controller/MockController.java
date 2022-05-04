package com.personal.quantization.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.personal.quantization.mock.vo.QuantizationResult;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/mock")
public class MockController {

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@Autowired
	private OAuth2RestTemplate oAuth2RestTemplate;

	@GetMapping("/member")
	public String member() {

		ResponseEntity<QuantizationResult> entity = oAuth2RestTemplate.getForEntity("http://localhost:38370/quantization/product/list",
		QuantizationResult.class); QuantizationResult body = entity.getBody();
		System.out.println("body: " + body);
		return "member";
	}
	
}
