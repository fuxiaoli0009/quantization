package com.personal.quantization.common;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.personal.quantization.constant.Constants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

	@Autowired
    private RedisTemplate<String, String> limitRedisTemplate;
	
	@Autowired
	private DefaultRedisScript<Long> rateLimitLua;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        assert handler instanceof HandlerMethod;
        HandlerMethod method = null;
		try {
			method = (HandlerMethod) handler;
		} catch (Exception e) {
			return true;
		}
        RateLimit rateLimit = method.getMethodAnnotation(RateLimit.class);
        //当前方法上有我们自定义的注解
        if (rateLimit != null) {
            //获得单位时间内限制的访问次数
            int count = rateLimit.count();
            String key = rateLimit.key();
            //获得限流单位时间（单位为s）
            String time = String.valueOf(rateLimit.time());
            boolean ipLimit = rateLimit.ipLimit();
            //拼接 redis中的key
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.RATE_LIMIT_KEY).append(key).append(":");
            //如果需要限制ip的话
            if(ipLimit){
                sb.append(getIpAddress(request)).append(":");
            }
            List<String> keys = Collections.singletonList(sb.toString());
           //执行lua脚本
			Long execute = limitRedisTemplate.execute(rateLimitLua, keys, time, String.valueOf(count)); 
			log.info("execute result:{}", execute); 
			assert execute != null; 
			if (-1 == execute.intValue()) { 
				response.setStatus(901);
				response.setCharacterEncoding("utf-8");
			    response.setContentType("application/json");
				response.getWriter().write(JSONObject.toJSONString("接口调用超过限流次数"));
				response.getWriter().flush(); response.getWriter().close(); 
				return false; 
			} else { 
				
			}
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
    
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) { 
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
    
    public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}