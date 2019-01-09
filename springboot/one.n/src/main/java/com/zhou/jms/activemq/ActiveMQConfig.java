package com.zhou.jms.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
public class ActiveMQConfig {

	@Bean(name="queue")
	public Queue queue()
	{
		return new ActiveMQQueue("test.queue");
	}
}
