package com.zhou.jms.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	Logger logger = LoggerFactory.getLogger(Consumer.class);
	@JmsListener(destination = "test.queue")
	public void receiveQueue(String text)
	{
		System.out.println("Consumer收到的报文为:"+text);
		System.out.println("=================");
		logger.info("Consumer收到的报文为:"+text); 
		logger.info("=================");
	}
}
