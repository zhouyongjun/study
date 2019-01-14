package com.zhou.test.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
@DisallowConcurrentExecution
public class SecondJob extends QuartzJob 
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
//		System.err.println("QuartzJob 1 :" + data.getString("jobKey"));
		Quartz.execute(data.getString("jobKey"));
//		System.err.println("QuartzJob 2 :" + data.getString("jobKey"));
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		
	}
}
