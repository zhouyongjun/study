package com.zhou;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = ApplicationDev.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestRedis {

	@Resource
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void testSet()
	{
		this.redisTemplate.opsForValue().set("study","Java");
		System.err.println("value:"+this.redisTemplate.opsForValue().get("study"));
	}
}
