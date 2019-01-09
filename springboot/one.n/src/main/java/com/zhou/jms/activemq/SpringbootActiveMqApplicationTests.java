package com.zhou.jms.activemq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootActiveMqApplicationTests {
	@Autowired
	Producer producer;
	
	@Test
	public void contextLoads()
	{
		Destination destination = new ActiveMQQueue("test.queue");
		for (int i=0;i<5;i++)
		{
			producer.sendMessage(destination, "生产者发送消息"+i);
		}
	}
}

