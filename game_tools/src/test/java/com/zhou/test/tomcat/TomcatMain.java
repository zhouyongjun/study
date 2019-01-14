package com.zhou.test.tomcat;

import com.zhou.core.http.server.TomcatContext;
import com.zhou.core.http.server.TomcatServer;
import com.zhou.core.http.server.servlet.impl.DefaultServlet;
import com.zhou.core.http.server.servlet.impl.DownloadServlet;
import com.zhou.core.http.server.servlet.impl.UploadServlet;
	/**
	 * http web≤‚ ‘
	 * @author zhouyongjun
	 *
	 */
public class TomcatMain {
	public static void main(String[] args) {
		try {
			TomcatServer server = new TomcatServer();
			
			TomcatContext context = server.registerTomcatContext("/","",new DownloadServlet(),new UploadServlet());
//			context.getContext().addServletMapping("/index.jps", "");
//			server.registerTomcatContext("/tt","tt",new DefaultServlet(),new DownloadServlet(),new UploadServlet());
//		     WebResourceRoot resources = new StandardRoot(ctx);
//		        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
//		                additionWebInfClasses.getAbsolutePath(), "/"));
//		        ctx.setResources(resources);

			server.startup();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
