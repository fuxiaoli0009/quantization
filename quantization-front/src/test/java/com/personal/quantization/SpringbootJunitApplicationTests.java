package com.personal.quantization;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootJunitApplicationTests {

	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private DefaultRedisScript<Long> rateLimitLua;
	
	@Autowired
	private DefaultRedisScript<Long> testLua;
	
    @Test
    public void redisLuaTest() {
    	try {
		    List<String> keys = new ArrayList<>();
		    keys.add("luateststr");
		    Long result = redisTemplate.execute(testLua, keys);
		    System.out.println("result:" + result);
		} catch (Exception e) {
		    e.printStackTrace();
		}
    }
    
    @Test
    public static void test() {
		System.out.println(100&16385);
	}

}