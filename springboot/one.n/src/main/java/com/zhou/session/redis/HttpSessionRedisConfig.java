package com.zhou.session.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds=10)//设置失效时间
public class HttpSessionRedisConfig {
	/**
	 * 自定义redis配置
	 * @return
	 * @throws Exception
	 */
//	@Bean
//	 public JedisConnectionFactory connectionFactory() throws Exception {
//	 return new JedisConnectionFactory();
//	}
}
