package com.zhou.service.impl;

import javax.annotation.Resource;
import javax.jms.Queue;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import com.zhou.service.IActiveMQProducerService;

@Service
public class ActiveMQProducerServiceImpl implements IActiveMQProducerService {
	@Resource(name="queue")
	Queue queue;
	@Resource
	JmsMessagingTemplate template;
	@Override
	public void sendMessage(String message) {
		template.convertAndSend(queue,message);
	}

}
