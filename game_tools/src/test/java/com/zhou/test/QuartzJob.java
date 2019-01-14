package com.zhou.test;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.UnableToInterruptJobException;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJob implements InterruptableJob 
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		
	}
}
