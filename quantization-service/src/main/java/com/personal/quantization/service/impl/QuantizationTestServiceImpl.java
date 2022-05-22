package com.personal.quantization.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.personal.quantization.center.api.QuantizationCenterClient;
import com.personal.quantization.mapper.QuantizationTestMapper;
import com.personal.quantization.mapperauth.AuthTestMapper;
import com.personal.quantization.model.AuthTest;
import com.personal.quantization.model.CenterTest;
import com.personal.quantization.model.QuantizationError;
import com.personal.quantization.model.QuantizationTest;
import com.personal.quantization.service.QuantizationTestService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuantizationTestServiceImpl implements QuantizationTestService {
	
	private int stat1 = 0;
	private int stat2 = 0;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private QuantizationTestMapper quantizationTestMapper;
	
	@Autowired
	private QuantizationCenterClient centerClient;
	
	@Autowired
	private AuthTestMapper authTestMapper;
    
	@Override
	@Transactional(value="mongoTransactionManager")
	public void mongoTransactionTest() {
		QuantizationError error1 = new QuantizationError();
		error1.setQuantizationName("test1");
		error1.setQuantizationName("test1");
		error1.setMessage("test1 test1 test1");
		mongoTemplate.insert(error1);
		QuantizationError error2 = new QuantizationError();
		error2.setQuantizationName("test2");
		error2.setQuantizationName("test2");
		error2.setMessage("test2 test2 test2");
		mongoTemplate.insert(error2);
		List<QuantizationError> list = mongoTemplate.findAll(QuantizationError.class);
		log.info(JSON.toJSONString(list));
	}

	@Override
	@Transactional
	public void mysqlTransactionTest() {
		QuantizationTest test = new QuantizationTest();
		test.setQuantizationCode("abc123");
		test.setQuantizationName("示范点是哦扫地机");
		quantizationTestMapper.saveQuantizationTest(test);
		log.info("成功保存mysql");
		int i = 1/0;
		log.info("i"+i);
	}
	
	@Override
	@Transactional
	public void shardingTransactionTest() {
		QuantizationTest test = new QuantizationTest();
		test.setQuantizationCode("abc123");
		test.setQuantizationName("示范点是哦扫地机");
		quantizationTestMapper.saveQuantizationTest(test);
		log.info("成功保存mysql");
		int i = 1/0;
		log.info("i"+i);
	}

	// 开启事务，由于使用jta+atomikos解决分布式事务，所以此处不必再指定事务
	@Transactional
	@Override
	public void twoMysqlTransactionTest() {
		QuantizationTest test = new QuantizationTest();
		test.setQuantizationCode("abc123");
		test.setQuantizationName("示范点是哦扫地机");
		quantizationTestMapper.saveQuantizationTest(test);
		AuthTest authTest = new AuthTest();
		authTest.setAuthCode("abc123");
		authTest.setAuthName("示范点是哦扫地机");
		authTestMapper.saveAuthTest(authTest);
		int i = 1/0;
		log.info("i"+i);
	}
	
	/**
	 * 单服务两个mysql：seatea事务
	 */
	//@Transactional
	//@GlobalTransactional
	@Override
	public void twoMysqlTransactionBySeataTest() {
		
		twoMysqlSelectTest();
		QuantizationTest test = new QuantizationTest();
		test.setQuantizationCode("abc123");
		test.setQuantizationName("示范点是哦扫地机");
		quantizationTestMapper.saveQuantizationTest(test);
		AuthTest authTest = new AuthTest();
		authTest.setAuthCode("abc123");
		authTest.setAuthName("示范点是哦扫地机");
		authTestMapper.saveAuthTest(authTest);
	}
	
	/**
	 * 两个微服务跨库分布式事务-seata
	 */
	//@GlobalTransactional
	@Override
	public void twoMicroServiceTransactionBySeataTest() {
		
		List<QuantizationTest> quantizationList = quantizationTestMapper.selectQuantizationTests();
		log.info("quantizationTestList:{}", quantizationList.size());
		QuantizationTest test = new QuantizationTest();
		test.setQuantizationCode("abc123");
		test.setQuantizationName("示范点是哦扫地机");
		quantizationTestMapper.saveQuantizationTest(test);
		log.info("quantization 保存成功");
		List<CenterTest> centerTestList = centerClient.selectCenterTests();
		log.info("centerTestList:{}", centerTestList.size());
		CenterTest centerTest = new CenterTest();
		centerTest.setCenterCode("sdfsfd");
		centerTest.setCenterName(String.valueOf(new Date()));
		centerClient.saveCenterTest(centerTest);
		log.info("center 保存成功");
		if((int)(10*Math.random())==5) {
			stat1++;
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("stat1:{}", stat1);
			int i = 1/0;
		}
		stat2++;
		log.info("stat1:{}, stat2:{}", stat1, stat2);
	}
	
	@Override
	@Transactional(value="mongoMysqlTransactionTest")
	public void mongoMysqlTransactionTest() {
		
		QuantizationError error1 = new QuantizationError();
		error1.setQuantizationName("test1");
		error1.setQuantizationName("test1");
		error1.setMessage("test1 test1 test1");
		mongoTemplate.insert(error1);
		log.info("成功保存mongo");
		QuantizationTest test = new QuantizationTest();
		test.setQuantizationCode("abc123");
		test.setQuantizationName("示范点是哦扫地机");
		quantizationTestMapper.saveQuantizationTest(test);
		log.info("成功保存mysql");
		int i = 1/0;
		log.info("i"+i);
	}

	@Override
	public void twoMysqlSelectTest() {
		List<QuantizationTest> quantizationTests = quantizationTestMapper.selectQuantizationTests();
		List<AuthTest> authTests = authTestMapper.selectAuthTests();
		log.info("quantizationTests:{}", JSON.toJSONString(quantizationTests));
		log.info("authTests:{}", JSON.toJSONString(authTests));
	}
	
}
