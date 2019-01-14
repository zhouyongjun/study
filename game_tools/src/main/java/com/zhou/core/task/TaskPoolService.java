package com.zhou.core.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.config.ToolsConfig;
import com.zhou.core.email.EmailConfig;
import com.zhou.core.email.EmailService;
import com.zhou.util.CParam;
import com.zhou.util.CalendarUtil;

public final class TaskPoolService implements ConsoleSystem{
	public static long SECONDS_PER_DAY = TimeUnit.DAYS.toSeconds(1);
	public static long SECONDS_PER_HOUR = TimeUnit.HOURS.toSeconds(1);
	public static long SECONDS_PER_MINTUE = TimeUnit.MINUTES.toSeconds(1);
	public static long SECONDS_PER_SECOND = TimeUnit.SECONDS.toSeconds(1);
	private static TaskPoolService instance = new TaskPoolService();
	ScheduledThreadPoolExecutor pool;
	boolean isStartUp = false;
	Taskable minuteTaskable = new Taskable() {
		
		@Override
		public void run() {
			try {
//				logger.debug("Console App minutely task run.");
				if (!EmailConfig.EMAIL_TICK_SWITCH)
				{
					return;
				}
				EmailService.getInstance().tick(CParam.newInstance());
			} catch (Throwable e) {
				logger.error(e);
			}
		}
	};
	private static final Logger logger = LogManager.getLogger(TaskPoolService.class);
	private TaskPoolService() {
		
	}
	
	public boolean isShutdown()
	{
		return pool == null || pool.isShutdown();
	}
	
	public void startup(Object... params) throws Exception{
		if (isShutdown())
		{
			pool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(ToolsConfig.TASK_POOL_SIZE
					, new ThreadFactoryBuilder().setNameFormat("tools-task-%d").build());
//			scheduleAtFixedRate(minuteTaskable, 0, EmailConfig.scheduleAtFixedRate_second, TimeUnit.SECONDS);
		}
		logger.info("Console App "+this.getClass().getName()+" startup!");
		isStartUp = true;
	}
	
	public void shutdown(Object... params) throws Exception {
		if (pool == null) {
			return;
		}
		pool.shutdown();
		isStartUp = false;
		logger.info("TaskPool shutdown...");
	}
	
	public ScheduledFuture<?> scheduleAtFixedRate(Taskable task,long initialDelay,long period,TimeUnit timeUnit) {
		return pool.scheduleAtFixedRate(task, initialDelay, period, timeUnit);
	}

	public ScheduledFuture<?> schedule(Taskable task) {
		return pool.schedule(task, 0,  TimeUnit.MILLISECONDS);
	}
	
	public ScheduledFuture<?> schedule(Taskable task,long initialDelay,TimeUnit timeUnit) {
		return pool.schedule(task, initialDelay,  timeUnit);
	}
	
	public static final TaskPoolService getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		try {
			long init = CalendarUtil.getSecondsToClock(0, 0, 1);
			System.out.println("init : " + init+",all : " + CalendarUtil.DAY/CalendarUtil.SECOND);
			TaskPoolService.getInstance().startup();
			TaskPoolService.getInstance().scheduleAtFixedRate(new Taskable() {
				int num;
				@Override
				public void run() {
					logger.info("Taskable : " + num++);
				}
			},init , TaskPoolService.SECONDS_PER_DAY, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.TASK_POOL;
	}
	@Override
	public boolean isStartUp() {
		return isStartUp;
	}
}
