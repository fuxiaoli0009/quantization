package com.personal.quantization.center.service;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RocketMQService {

	@Autowired
    private RocketMQTemplate rocketMQTemplate;

    public String pushMessage(String str) {
        rocketMQTemplate.convertAndSend("quantization-topic","你好,RocketMq =>" + str);
        return "success";
    }
}
