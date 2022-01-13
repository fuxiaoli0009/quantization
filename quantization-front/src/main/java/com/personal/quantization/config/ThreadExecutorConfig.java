package com.personal.quantization.config;
 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
 
@Configuration
@EnableAsync
public class ThreadExecutorConfig {
 
    /**
     * 核心线程数
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    private int maxPoolSize = Integer.MAX_VALUE;
    /**
     * 线程销毁时间
     */
    private Long keepAliveTime = 60L;
    /**
     * 任务队列
     */
    private SynchronousQueue synchronousQueue = new SynchronousQueue<>();
    /**
     * 线程名称
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("datas-thread-%d").build();
 
    @Bean
    public ExecutorService myDataThreadPool() {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, synchronousQueue, namedThreadFactory);
    }
}