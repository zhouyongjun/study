package com.zhou.core.gm.bs;

import java.util.concurrent.ScheduledFuture;

import com.zhou.core.http.server.TomcatContext;
import com.zhou.core.http.server.TomcatServer;
import com.zhou.core.http.server.servlet.AbstractServlet;
import com.zhou.core.task.DefaultScheduledPool;
import com.zhou.core.task.Taskable;


public class GMWeb {
	 TomcatServer server = null;
	 ScheduledFuture<?> schaduledFuture = null;
	 private static GMWeb instance = new GMWeb();
	 private GMWeb()
	 {
		 
	 }
	 public static GMWeb getInstance() {
		return instance;
	}
	 public void start()
	 {
		 schaduledFuture = DefaultScheduledPool.getInstance().schedule(new Taskable() {
			
			@Override
			public void run() {
				try {
					server = new TomcatServer();
					register(server);
					server.startup(9);
				}  catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	 }
	 
	 private void register(TomcatServer server) throws Exception {
			String[][]  contextConfigs=
				{
					{"/",""},//contextPath, docBase
					{"/gm","gm"},//contextPath, docBase
				};
			String[][] servletConfigs = 
				{
					{},
					{
						"com.zhou.core.gm.bs.servlet.LoginServlet",
						"com.zhou.core.gm.bs.servlet.control.CreateNewServerServlet"
					},
				};
			for (int i=0;i<contextConfigs.length;i++)
			{
				TomcatContext context = server.registerTomcatContext(contextConfigs[i][0], contextConfigs[i][1]);
				for (int j=0;j<servletConfigs[i].length;j++)
				{
					AbstractServlet servlet = (AbstractServlet)Class.forName(servletConfigs[i][j]).newInstance();
					context.registerServlet(servlet);	
				}
			}
			
		}

	public static void main(String[] args) {
		GMWeb.getInstance().start();
	}



	public TomcatServer getServer() {
		return server;
	}
	
}
