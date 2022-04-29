package com.personal.quantization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

@Component
public class RateLimitConfig {

	@Bean("rateLimitLua")
    public DefaultRedisScript<Long> redisluaScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/rateLimit.lua"))); 
        redisScript.setResultType(Long.class);
        return redisScript;
    }
	
	@Bean("testLua")
    public DefaultRedisScript<Long> redisluaScriptTest() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/luatest.lua"))); 
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
