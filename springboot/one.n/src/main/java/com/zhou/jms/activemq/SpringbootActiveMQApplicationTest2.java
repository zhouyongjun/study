package com.zhou.jms.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhou.service.IActiveMQProducerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpringbootActiveMQApplicationTest2 {
	@Autowired
	IActiveMQProducerService service;
	@Test
	public void test()
	{
//		service.sendMessage("测试。。。。");
		for (int i=0;i<5;i++)
		{
			service.sendMessage("生产者发送消息"+i);
		}
	}
}
