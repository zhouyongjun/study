package com.zhou.test.redis;


public class RedisService {
	public static void main(String[] args) {
		PropertyFileReader.load();
		RedisSession redis = new RedisSession(false);
		redis.incr("test");
		System.out.println(redis.get("test"));
//		redis.close();
		
		DefaultPubSub pubsub = new DefaultPubSub();
		redis.subscribe(pubsub, "ttt");
		while (true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
