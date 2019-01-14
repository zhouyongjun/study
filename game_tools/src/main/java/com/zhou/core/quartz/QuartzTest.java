package com.zhou.core.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuartzTest {
	private static final Logger logger = LogManager.getLogger(QuartzTest.class);
	static AtomicInteger NUM = new AtomicInteger();
	public static void main(String[] args) {
		
		Quartz.init();
		test1(1,1);
		test1(1,3);
		
	}
	private static void test1(final int k, int sec) {
		Quartz.schedule("*/"+sec+" * * * * ?", new IQuartz() {
					
					@Override
					public void execute() {
						long time = System.currentTimeMillis();
						NUM.incrementAndGet();
						System.err.println("test"+k+"........ before : " +NUM.get());
						logger.info("test"+k+"........ before : " +NUM.get());
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.err.println("test"+k+"........ end  : "+NUM.get() +",T="+(System.currentTimeMillis()-time));
						logger.info("test"+k+"........ end  : "+NUM.get() +",T="+(System.currentTimeMillis()-time));
					}
				});		
	}
}
