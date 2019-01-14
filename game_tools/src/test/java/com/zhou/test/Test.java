package com.zhou.test;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class Test {
	public static void main(String[] args) {
		
	System.out.println(Math.pow(-8, 2));
	}
	
	private String addSchedule(String cron, IQuartz iJob) throws Exception {
		if (cron == null || cron.isEmpty()) {
			return "";
		}
		StdSchedulerFactory.getDefaultScheduler().start();
		try {
			String jobKey = "RA_QuartzJob_";
			String trigKey = "RA_QuartzTrig_" ;
			
			JobDetail job = JobBuilder.newJob(QuartzJob.class)
					.withIdentity(jobKey, Scheduler.DEFAULT_GROUP)
					.usingJobData("jobKey", jobKey)
					.build();

			CronTrigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(trigKey, Scheduler.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron))
					.build();

			StdSchedulerFactory.getDefaultScheduler().scheduleJob(job, trigger);
			return jobKey;
		}
		catch(Exception e) {
		}
		return "";
	}
}
