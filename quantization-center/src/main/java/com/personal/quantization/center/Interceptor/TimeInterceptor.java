package com.personal.quantization.center.Interceptor;

import java.lang.reflect.Method;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@Configuration
@Slf4j
public class TimeInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = proxy.invokeSuper(obj, args);
		log.info("【增强方法】{} 执行时间：{}", method.getName(), System.currentTimeMillis() - start);
		return result;
	}

}
