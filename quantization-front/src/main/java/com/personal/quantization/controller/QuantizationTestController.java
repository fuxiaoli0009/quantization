package com.personal.quantization.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.common.RateLimit;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/quantization/test")
@Slf4j
public class QuantizationTestController {
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;

	@RateLimit(key = "testGet", time = 30, count = 5,ipLimit = true)
	@ApiOperation(value = "redis+lua", httpMethod = "GET")
    @RequestMapping(value = "/redisFirst", method = RequestMethod.GET)
    public void redisFirst(){
		try {
			
		    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		    redisScript.setResultType(Long.class);
		    redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/luatest.lua")));
		    //参数一：redisScript，参数二：key列表，参数三：arg（可多个）
		    List<String> keys = new ArrayList<>();
		    keys.add("luateststr");
		    Long result = redisTemplate.execute(redisScript, keys);
		    System.out.println("result" + result);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(100%16385);
	}
}
