package com.zhou.test;

import org.apache.commons.lang.StringUtils;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {
	public static void main(String[] args) {
		RateLimiter limit = RateLimiter.create(100);
		for(int i=0;i<10;i++)
		{
			double waitTime = limit.acquire();
			System.out.println(waitTime);
		}
		System.err.println(StringUtils.isNumeric(""));
	}
}
