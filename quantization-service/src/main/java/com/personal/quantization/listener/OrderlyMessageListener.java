package com.personal.quantization.listener;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = "quantization-topic", selectorExpression = "orderly" ,consumerGroup = "quantization-front-group", consumeMode = ConsumeMode.ORDERLY)
public class OrderlyMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        // 打印出消息内容
    	log.info(message);
    	//throw new RuntimeException("我就是故意抛出一个异常");
    }
}