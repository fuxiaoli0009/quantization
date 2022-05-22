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
import com.personal.quantization.service.QuantizationTestService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/quantization/test")
@Slf4j
public class QuantizationTestController {
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private QuantizationTestService quantizationTestService;

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
		    log.info("result:{}", result);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 ******************************************************************************
	 ********************************** 事务相关 ***********************************
	 ******************************************************************************
	 */
	@ApiOperation(value = "mongoTransactionTest", httpMethod = "GET")
    @RequestMapping(value = "/mongoTransactionTest", method = RequestMethod.GET)
    public void mongoTransactionTest(){
		quantizationTestService.mongoTransactionTest();
	}
	
	@ApiOperation(value = "mysqlTransactionTest", httpMethod = "GET")
    @RequestMapping(value = "/mysqlTransactionTest", method = RequestMethod.GET)
    public void mysqlTransactionTest(){
		quantizationTestService.mysqlTransactionTest();
	}
	
	@ApiOperation(value = "shardingTransactionTest", httpMethod = "GET")
    @RequestMapping(value = "/shardingTransactionTest", method = RequestMethod.GET)
    public void shardingTransactionTest(){
		quantizationTestService.shardingTransactionTest();
	}
	
	@ApiOperation(value = "twoMysqlTransactionTest", httpMethod = "GET")
    @RequestMapping(value = "/twoMysqlTransactionTest", method = RequestMethod.GET)
    public void twoMysqlTransactionTest(){
		quantizationTestService.twoMysqlTransactionTest();
	}
	
	@ApiOperation(value = "twoMysqlTransactionBySeataTest", httpMethod = "GET")
    @RequestMapping(value = "/twoMysqlTransactionBySeataTest", method = RequestMethod.GET)
    public void twoMysqlTransactionBySeataTest(){
		quantizationTestService.twoMysqlTransactionBySeataTest();
	}
	
	@ApiOperation(value = "twoMicroServiceTransactionBySeataTest", httpMethod = "GET")
    @RequestMapping(value = "/twoMicroServiceTransactionBySeataTest", method = RequestMethod.GET)
    public void twoMicroServiceTransactionBySeataTest(){
		try {
			quantizationTestService.twoMicroServiceTransactionBySeataTest();
		} catch (Exception e) {
			log.info("");
		}
	}
	
	@ApiOperation(value = "twoMysqlTransactionTest", httpMethod = "GET")
    @RequestMapping(value = "/twoMysqlSelectTest", method = RequestMethod.GET)
    public void twoMysqlSelectTest(){
		quantizationTestService.twoMysqlSelectTest();
	}
	
	@ApiOperation(value = "mongoMysqlTransactionTest", httpMethod = "GET")
    @RequestMapping(value = "/mongoMysqlTransactionTest", method = RequestMethod.GET)
    public void mongoMysqlTransactionTest(){
		quantizationTestService.mongoMysqlTransactionTest();
	}
	
}
