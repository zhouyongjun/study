package com.zhou.cache.ehcache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EhcacheConfig extends CachingConfigurerSupport{
	@Bean
	public CacheManager ehCacheManager()
	{
		return new EhCacheCacheManager();
	}
}
