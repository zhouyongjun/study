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
import com.zhou.util.CalendarUtil;

public final class DefaultScheduledPool implements ConsoleSystem{
	private static final Logger logger = LogManager.getLogger(DefaultScheduledPool.class);
	public static long SECONDS_PER_DAY = TimeUnit.DAYS.toSeconds(1);
	public static long SECONDS_PER_HOUR = TimeUnit.HOURS.toSeconds(1);
	public static long SECONDS_PER_MINTUE = TimeUnit.MINUTES.toSeconds(1);
	public static long SECONDS_PER_SECOND = TimeUnit.SECONDS.toSeconds(1);
	private static DefaultScheduledPool instance = new DefaultScheduledPool();
	ScheduledThreadPoolExecutor pool;
	boolean isStartUp = false;
	
	private DefaultScheduledPool() {
		startup();
	}
	
	public boolean isShutdown()
	{
		return pool == null || pool.isShutdown();
	}
	
	public void startup(Object... params) {
		if (isShutdown())
		{
			pool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10
					, new ThreadFactoryBuilder().setNameFormat("tools-task-%d").build());
		}
		logger.info("Console App "+this.getClass().getName()+" startup!");
		isStartUp = true;
	}
	
	public void shutdown(Object... params) {
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
	
	public static final DefaultScheduledPool getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		try {
			long init = CalendarUtil.getSecondsToClock(0, 0, 1);
			System.out.println("init : " + init+",all : " + CalendarUtil.DAY/CalendarUtil.SECOND);
			DefaultScheduledPool.getInstance().startup();
			DefaultScheduledPool.getInstance().scheduleAtFixedRate(new Taskable() {
				int num;
				@Override
				public void run() {
					logger.info("Taskable : " + num++);
				}
			},init , DefaultScheduledPool.SECONDS_PER_DAY, TimeUnit.SECONDS);
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
