package com.personal.quantization.center.service;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RocketMQService {

	@Autowired
    private RocketMQTemplate rocketMQTemplate;
	
	@Value("${rocketmq.topic}")
	private String topic; 
	
	@Value("${rocketmq.tag}")
	private String tag; 

	/**
	 * 发送普通消息
	 * @param str
	 * @return
	 */
    public String pushMessage(String str) {
        rocketMQTemplate.convertAndSend(topic, "你好,RocketMq =>" + str);
        return "success";
    }

    /**
     * 发送顺序消息
     */
	public void sendMessageOrderly() {
		//顺序消费通过hashKey来确定他们在哪个queue
        for(int i=0;i<10000;i++){
            SendResult result = rocketMQTemplate.syncSendOrderly(topic + ":" + tag, "order:"+String.valueOf(i), "order");
            log.info("SendResult: {}", JSON.toJSONString(result));
        }

        //顺序消费通过hashKey来确定他们在哪个queue
        for(int i=0;i<100;i++){
            rocketMQTemplate.syncSendOrderly(topic + ":" + tag, "orderOne:"+String.valueOf(i), "orderOne");
        }
        //正常发送消费
		/*
		 * for(int i=0;i<100;i++){ rocketMQTemplate.convertAndSend("quantization-topic:"
		 * + tag, "order:"+String.valueOf(i)); }
		 */

	}
}
