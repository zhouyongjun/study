package com.zhou.test.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Jedis session pool
 */
public class RedisSessionUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionUtil.class);

    private JedisPool jedisPool = null;

    private RedisSessionUtil() {
        initPool();
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

    public Jedis getGlobalResource() {
//        return roundRobinGlobalPool.getResource();
        return null;
    }

    public void close(Jedis resource) {
        jedisPool.returnResource(resource);
    }

    public void closeBroken(Jedis resource) {
        jedisPool.returnBrokenResource(resource);
    }

    private void initPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(PropertyFileReader.getInt("redis.pool.maxActive"));
        poolConfig.setMaxIdle(PropertyFileReader.getInt("redis.pool.maxIdle"));
        
        poolConfig.setMaxWaitMillis(PropertyFileReader.getLong("redis.pool.maxWait"));
        poolConfig.setTestOnBorrow(PropertyFileReader.getBooolean("redis.pool.testOnBorrow"));
        poolConfig.setTestOnReturn(PropertyFileReader.getBooolean("redis.pool.testOnReturn"));
        int useIdle = PropertyFileReader.getInt("redis.pool.useIdle",Protocol.DEFAULT_DATABASE);
        String redisIp = PropertyFileReader.getString("redis.ip");
        int redisPort = PropertyFileReader.getInt("redis.port");
        jedisPool = new JedisPool(poolConfig, redisIp, redisPort,Protocol.DEFAULT_TIMEOUT,null,useIdle);
        
        logger.info("init local redis pool redisIp:" + redisIp + " redisPort:" + redisPort+" useIdle:"+useIdle+"  sucess!");
    }

    private static class LazyHolder {
        private static final RedisSessionUtil instance = new RedisSessionUtil();
    }

    public static RedisSessionUtil getInstance() {
        return LazyHolder.instance;
    }
}
