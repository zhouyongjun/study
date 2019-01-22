package com.zhou.test;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.ClusterServersConfig;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;

public class RedissonTest {
	private static Config config;
	private static ClusterServersConfig clusterServersConfig;
	private static String address="127.0.0.1:6379";
	public static @BeforeClass void init()
	{
		config = new Config();
		/*使用集群模式*/
		clusterServersConfig = config.useClusterServers();
		clusterServersConfig.addNodeAddress(address);
		clusterServersConfig.setMasterConnectionPoolSize(100);
		clusterServersConfig.setSlaveConnectionPoolSize(100);
		clusterServersConfig.setTimeout(100);
	}
	@Test
	public void test()
	{
		RedissonClient redisson = null;
		try {
			redisson = Redisson.create(config);
			RLock lock = redisson.getLock("testLock");
			/*
			 * 获取锁
			 * 第 1 个参数用于设定分布式锁的租约时间，而第
				2 个参数则为时间单位
				使用这种方式意味着在某一个获取到锁的线程未释放锁之
				前，其他线程只能够在队列中阻塞等待，这和 InnoDB 引擎提供的行锁机制如出一辙，
				并发越高等待的线程越多
				因此，在并发较大的情况下，建议大家使用 tryLock
			 */
			lock.lock(20, TimeUnit.MILLISECONDS);
			//释放锁
			lock.unlock();
		
			/*尝试获取锁
			 * 
			 * 在tryLock（）方法中开发人员可以通过参数“waitTime”来设定获取分布式锁的等
				待时间，超出规定的时间阔值后，线程将不再继续等待拿锁：那么为了提升库存扣
				减的成功率，可以在获取锁失败后尝试多次。相比 lock（）方法的拿锁方式，后者在并
				发较大的情况下不会使分布式锁沦为系统瓶颈，但是商品库存的扣减成功率会受到一定影响。
			 * */
			boolean result = lock.tryLock(10,20,TimeUnit.MILLISECONDS);
			if (result)
			{
				lock.forceUnlock();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (redisson !=null)
			{
				redisson.shutdown();
			}
		}
		
	}
}
