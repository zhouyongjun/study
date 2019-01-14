package com.zhou.test.tomcat.my;

import java.io.File;

import com.zhou.core.http.server.TomcatContext;
import com.zhou.core.http.server.TomcatServer;
import com.zhou.core.http.server.WebConfig;
import com.zhou.core.http.server.servlet.AbstractServlet;


public class WebService {
	 TomcatServer server = null;
	
	 private static WebService instance = new WebService();
	 private WebService()
	 {
		 
	 }
	 public static WebService getInstance() {
		return instance;
	}
	 public void start()
	 {
			try {
				server = new TomcatServer();
				register(server);
				server.startup(2);
			}  catch (Throwable e) {
				e.printStackTrace();
			}
		
	 }
	 
	 private void register(TomcatServer server) throws Exception {
		 String project_path = new File("").getAbsolutePath();
			String app_base =project_path+WebConfig.getString("http.app.base.dir", "/webapps");
			String[][]  contextConfigs=
				{
					{"/",app_base},//contextPath, docBase
				};
			String[][] servletConfigs = 
				{
					{
						"com.zhou.test.tomcat.TestServlet"
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
		WebService.getInstance().start();
	}



	public TomcatServer getServer() {
		return server;
	}
	
}
