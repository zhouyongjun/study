package com.zhou.test.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.util.CalendarUtil;

public class QuartzTest {
	private static final Logger logger = LogManager.getLogger(QuartzTest.class);
	static AtomicInteger NUM = new AtomicInteger();
	public static void main(String[] args) {
		
		Quartz.init();
//		test1(1,QuartzJob.class,1,5000);
		test1(1,QuartzJob.class,1,3000);
		test1(2,SecondJob.class,1,3000);
		
	}
	private static void test1(final int k,final Class<? extends QuartzJob> jobClass, int sec,final long sleepS) {
		
		Quartz.schedule("*/"+sec+" * * * * ?",jobClass, new IQuartz() {
					
					@Override
					public void execute() {
						long time = System.currentTimeMillis();
						int num = NUM.incrementAndGet();
						System.err.println(CalendarUtil.format(System.currentTimeMillis())+" test"+k+"<"+jobClass.getSimpleName()+">........ before : " +num);
//						logger.info("test"+k+"........ before : " +NUM.get());
						try {
							Thread.sleep(sleepS);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.err.println(CalendarUtil.format(System.currentTimeMillis())+" test"+k+"<"+jobClass.getSimpleName()+">........ end  : "+num +",T="+(System.currentTimeMillis()-time));
//						logger.info("test"+k+"........ end  : "+NUM.get() +",T="+(System.currentTimeMillis()-time));
					}
				});		
	}
	
}
