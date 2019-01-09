package com.zhou.cache.redis;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/*@Configuration
@EnableCaching*/
public class RedisConfig extends CachingConfigurerSupport{
//	
//	/*
//     * 定义缓存数据 key 生成策略的bean 包名+类名+方法名+所有参数
//     */
//	@Bean
//	public KeyGenerator wiselyKeyGenerator()
//	{
//		return new KeyGenerator() {
//			@Override
//			public Object generate(Object target, Method method, Object... params) {
//				 StringBuilder sb = new StringBuilder();
//			     sb.append(target.getClass().getName());
//			     sb.append(method.getName());
//			     for (Object obj : params) {
//			     sb.append(obj.toString());
//			     }
//			     return sb.toString();
//			}
//		};
//	}
//	
//	 /*
//     * 要启用spring缓存支持,需创建一个 CacheManager的 bean，CacheManager 接口有很多实现，这里Redis 的集成，用
//     * RedisCacheManager这个实现类 Redis 不是应用的共享内存，它只是一个内存服务器，就像 MySql 似的，
//     * 我们需要将应用连接到它并使用某种“语言”进行交互，因此我们还需要一个连接工厂以及一个 Spring 和 Redis 对话要用的
//     * RedisTemplate， 这些都是 Redis 缓存所必需的配置，把它们都放在自定义的 CachingConfigurerSupport 中
//     */
//	@Bean
//	public CacheManager cacheManager(RedisTemplate redisTemplate)
//	{
//		  // cacheManager.setDefaultExpiration(60);//设置缓存保留时间（seconds）
//		return new RedisCacheManager(redisTemplate);
//	}
//	
//	// 1.项目启动时此方法先被注册成bean被spring管理,如果没有这个bean，则redis可视化工具中的中文内容（key或者value）都会以二进制存储，不易检查。
//	@Bean
//	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory)
//	{
//		StringRedisTemplate template = new StringRedisTemplate(factory);
//		
//		  //set key serializer
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        template.setKeySerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//        
//		Jackson2JsonRedisSerializer<? extends Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		//>>>>>>>>>>>>>
//		/*
//		 * 测试证明：如果没有此2行代码，会导致序列化对象存储到redis 不成功，只能换成MAP形式存储
//		 */
//		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		//>>>>>>>>>>>>>		
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//		
////		template.setDefaultSerializer(jackson2JsonRedisSerializer);
//		template.setValueSerializer(jackson2JsonRedisSerializer);
//		template.afterPropertiesSet();
//		return template;
//
//	}
//	
//	
//	/**
//     * 配置RedisTemplate
//     * 设置添加序列化器
//     * key 使用string序列化器
//     * value 使用Json序列化器
//     * 还有一种简答的设置方式，改变defaultSerializer对象的实现。
//     * @return RedisTemplate<String, Object> :  若Object换成String则序列化对象会出现失败
//     */
// /*   @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        //StringRedisTemplate的构造方法中默认设置了stringSerializer
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        //set key serializer
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        template.setKeySerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//        //Value:支持 序列化对象(如果使用 RedisTemplate<String, String>这不会序列化成对象，只能转换成MAP数据)
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        //set value serializer
//        template.setDefaultSerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setConnectionFactory(factory);
//        template.afterPropertiesSet();
//        return template;
//	}*/
}
