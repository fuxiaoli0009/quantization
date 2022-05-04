package com.personal.quantization.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = "quantization-topic-test",consumerGroup = "quantization-front-group")
public class RocketmqListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        
        // 打印出消息内容
        log.info("==============================================>");
        System.out.println(message);
    }
}