package com.zhou.test.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class Quartz 
{
	private static final Logger logger = LogManager.getLogger(Quartz.class);
	private static Quartz instance = new Quartz();
	private Quartz() {}

	private long keyCounter = 0;
	private Map<String, IQuartz> jobs = new HashMap<String, IQuartz>();
	
	public static void init() {
		try {
			StdSchedulerFactory.getDefaultScheduler().start();
			logger.error("Quartz init success");
		} 
		catch (SchedulerException e) {
			logger.error("Quartz init error, " + e.getMessage());
		}
	}
	
	public static void shutdown() {
		try {
			instance.jobs.clear();
			StdSchedulerFactory.getDefaultScheduler().shutdown();
		} 
		catch (SchedulerException e) {
			logger.error("Quartz shutdown error, " + e.getMessage());
		}
	}
	private String addSchedule(String cron, IQuartz iJob) {
		return addSchedule(cron,QuartzJob.class, iJob);
		
	}
	private String addSchedule(String cron,Class<? extends Job> jobClass, IQuartz iJob) {
		if (cron == null || cron.isEmpty()) {
			logger.error("Quartz addSchedule error, cron="+cron);
			return "";
		}
		
		try {
			String jobKey = "RA_QuartzJob_" + (++keyCounter);
			String trigKey = "RA_QuartzTrig_" + keyCounter;
			jobs.put(jobKey, iJob);
			
			JobDetail job = JobBuilder.newJob(jobClass)
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
			logger.error("Quartz addSchedule error, " + e.getMessage());
		}
		return "";
	}
	
	private String addSchedule(Date date, IQuartz iJob) {
		if (date == null) {
			logger.error("Quartz addSchedule error, date="+date);
			return "";
		}
		
		try {
			String jobKey = "RA_QuartzJob_" + (++keyCounter);
			String trigKey = "RA_QuartzTrig_" + keyCounter;
			jobs.put(jobKey, iJob);
			
			JobDetail job = JobBuilder.newJob(QuartzJob.class)
					.withIdentity(jobKey, Scheduler.DEFAULT_GROUP)
					.usingJobData("jobKey", jobKey)
					.build();

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(trigKey, Scheduler.DEFAULT_GROUP)
					.startAt(date)
					.build();

			StdSchedulerFactory.getDefaultScheduler().scheduleJob(job, trigger);
			return jobKey;
		}
		catch(Exception e) {
			logger.error("Quartz addSchedule error, " + e.getMessage());
		}
		return "";
	}
	
	private void removeSchedule(String jobKey) {
		if (jobKey == null || jobKey.isEmpty()) {
			logger.info("removeSchedule error jobKey="+jobKey);
			return;
		}
		
		try {
			StdSchedulerFactory.getDefaultScheduler().deleteJob(JobKey.jobKey(jobKey));
			jobs.remove(jobKey);
		} 
		catch (SchedulerException e) {
			logger.error("Quartz removeSchedule deleteJob error, " , e);
		}
	}
	
	private void executeSchedule(String jobKey) {
		if (jobKey == null || jobKey.isEmpty()) {
			logger.info("executeSchedule error jobKey="+jobKey);
			return;
		}
		
		if (jobs.containsKey(jobKey)) {
			jobs.get(jobKey).execute();
		}
		else {
			removeSchedule(jobKey);
			logger.info("executeSchedule but jobKey="+jobKey+" not exist");
		}
	}
	
	public static String schedule(String cron, IQuartz iJob) {
		return instance.addSchedule(cron, iJob);
	}
	
	public static String schedule(String cron,Class<? extends QuartzJob> jobClass, IQuartz iJob) {
		return instance.addSchedule(cron,jobClass, iJob);
	}
	
	public static String schedule(Date date, IQuartz iJob) {
		return instance.addSchedule(date, iJob);
	}
	
	public static void stop(String jobKey) {
		instance.removeSchedule(jobKey);
	}
	
	protected static void execute(String jobKey) {
		instance.executeSchedule(jobKey);
	}
	
	public static void print() {
		try {
			logger.info(">>>>>>>>>> quartz print");
			
			Set<JobKey> jobKeys = StdSchedulerFactory.getDefaultScheduler().getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP));
			
			logger.info("exeJobs="+jobKeys.size()+", jobs="+instance.jobs.size());
			
			Iterator<JobKey> it = jobKeys.iterator();
			while(it.hasNext()) {
				String jobKey = it.next().toString();
				if (!instance.jobs.containsKey(jobKey)) {
					logger.info("bingo: key="+jobKey);
				}
			}
			logger.info("<<<<<<<<<< quartz print");
		}
		catch(Exception e) {
			
		}
	}
	
	public static void main(String[] args) {
		System.out.println(""+null);
	}
}
