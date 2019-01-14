package com.zhou.core.telnet.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.ToolsApp.SystemType;
import com.zhou.core.telnet.TelnetHandle;
import com.zhou.core.telnet.TelnetService;
	/**
	 * 默认telnet 管理类
	 * @author zhouyongjun
	 *
	 */
public class DefaultTelnetService extends TelnetService
{
  private static final Logger logger = LogManager.getLogger(DefaultTelnetService.class);
  private static ExecutorService exec;
  private Map<Integer, ConsoleRunnable> consoles = new HashMap<>();
  /**
   * 启动一个telnet服务
   */
  public void startConsole(int port, TelnetHandle service) throws Exception
  {
	  if (exec == null) throw new TelnetException("ExecutorService is null,please call startup method before.");
	    if (this.consoles.containsKey(port)) throw new TelnetException("repeat the same port["+port+"] console service...");
	    ConsoleRunnable runnable = new ConsoleRunnable(port,service);
	    this.consoles.put(port, runnable);
	    exec.execute(runnable);
	    logger.info("open telnet console service(port:" +  port + ") calss:"+service.getClass().getName());
  }
  	/**
  	 * 停止服务
  	 */
	@Override
	public void stopConsole(int port) throws Exception {
		ConsoleRunnable runnbale = null;
		if (this.consoles.containsKey(port)) 
		{
			runnbale = this.consoles.get(port);	
		}
		if (runnbale == null) 
		{
			logger.info("console port["+port+"]is stoped before.");
			return;
		}
		runnbale.isRunning = false;
		consoles.remove(port);
	}
	
	@Override
	public void shutdown(Object... params) throws Throwable {
		super.shutdown(params);
		exec.shutdown();
		logger.info("shutdown telnet console service...");
	}
	@Override
	public void handleWhenStartup(Object... params) throws Throwable {
		if(exec!= null && !exec.isShutdown()) {
			shutdown();
		}
		exec = Executors.newCachedThreadPool();
		logger.info("starup telnet console service...");
	}
	
	 class ConsoleRunnable implements Runnable {
		  int port;
		  TelnetHandle service;
		  boolean isRunning = true;
		public ConsoleRunnable(int port,TelnetHandle service) {
			this.port = port;
			this.service = service;
		}
		 public void run() {
	     ServerSocket localServerSocket = null;
	     try {
			localServerSocket = new ServerSocket(port);
		     while (isRunning)
		     {
		    	 try
			       {
			         Socket localSocket = localServerSocket.accept();
			         ConsoleRunnable runnable = consoles.get(Integer.valueOf(port));
			         exec.execute(new Terminal(localSocket, runnable.service));
			       }
			       catch (Throwable e)
			       {
			    	   logger.error(e);
			       } 
		     }
		     logger.info("console port["+port+"]is successful stoped.");
		   } catch (Throwable e) {
				logger.error(e);
			}finally{
				try {
					if (localServerSocket != null)localServerSocket.close();
				} catch (Throwable e1) {e1.printStackTrace();}
		}
	   }
		  
	  }
	  
	  public static void main(String[] paramArrayOfString)
	  {
	    try {
			DefaultTelnetService localDefaultConsoleManager = new DefaultTelnetService();
			localDefaultConsoleManager.startConsole(20000, new TelnetHandle()
			{
			  public String handle(String paramString)
			  {
				  System.err.println("ConsoleService Param:"+paramString);
			    return paramString;
			  }
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.TELENT;
	}

	  
}


