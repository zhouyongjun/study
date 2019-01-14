package com.zhou.test.redis;

import redis.clients.jedis.JedisPubSub;

public class DefaultPubSub extends JedisPubSub{

	@Override
	public void onMessage(String channel, String message) {
		super.onMessage(channel, message);
		System.out.println("onMessage  channel="+channel+",message="+message);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
		super.onPMessage(pattern, channel, message);
		System.out.println("onPMessage  pattern="+channel+",message="+message);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		super.onSubscribe(channel, subscribedChannels);
		System.out.println("onSubscribe  channel="+channel+",subscribedChannels="+subscribedChannels);
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		super.onUnsubscribe(channel, subscribedChannels);
		
		System.out.println("onUnsubscribe  channel="+channel+",subscribedChannels="+subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		super.onPUnsubscribe(pattern, subscribedChannels);
		System.out.println("onPUnsubscribe  pattern="+pattern+",subscribedChannels="+subscribedChannels);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		super.onPSubscribe(pattern, subscribedChannels);
		System.out.println("onPSubscribe  pattern="+pattern+",subscribedChannels="+subscribedChannels);
	}

	@Override
	public void unsubscribe() {
		// TODO Auto-generated method stub
		super.unsubscribe();
		System.out.println("unsubscribe");
	}

	@Override
	public void unsubscribe(String... channels) {
		// TODO Auto-generated method stub
		super.unsubscribe(channels);
		System.out.println("unsubscribe String...");
	}

	@Override
	public void subscribe(String... channels) {
		// TODO Auto-generated method stub
		super.subscribe(channels);
		
		System.out.println("subscribe  channels="+channels[0]);
	}

	@Override
	public void psubscribe(String... patterns) {
		// TODO Auto-generated method stub
		super.psubscribe(patterns);
	}

	@Override
	public void punsubscribe() {
		// TODO Auto-generated method stub
		super.punsubscribe();
	}

	@Override
	public void punsubscribe(String... patterns) {
		// TODO Auto-generated method stub
		super.punsubscribe(patterns);
	}

	@Override
	public boolean isSubscribed() {
		// TODO Auto-generated method stub
		return super.isSubscribed();
	}

	@Override
	public int getSubscribedChannels() {
		// TODO Auto-generated method stub
		return super.getSubscribedChannels();
	}
	
	
}
