package com.zhou.test.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

import com.zhou.core.http.server.servlet.impl.DefaultServlet;
import com.zhou.core.http.server.servlet.impl.DownloadServlet;
import com.zhou.core.http.server.servlet.impl.UploadServlet;

public class TestMain {
	public static void main(String[] args) {
	
		try {
			newT();
//			test();
//			old();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void old()
	{
		Tomcat tomcat = new Tomcat();
		//容器
		String path = new File("").getAbsolutePath() ;
		String appBase = path +"/webapps";
		tomcat.getHost().setAppBase(appBase);
		tomcat.setBaseDir(path);
		tomcat.setPort(8888);
		Context ctx = tomcat.addContext("/", appBase);
		
		ctx.addLifecycleListener(new Tomcat.DefaultWebXmlListener());
		//--------------------
		Wrapper servlet = Tomcat.addServlet(
                ctx, "jsp", "org.apache.jasper.servlet.JspServlet");
        servlet.addInitParameter("fork", "false");
        servlet.setLoadOnStartup(3);
        servlet.setOverridable(true);

        // Servlet mappings
//        ctx.addServletMapping("/", "default");
        ctx.addServletMapping("*.jsp", "jsp");
        ctx.addServletMapping("*.jspx", "jsp");

        // Sessions
        ctx.setSessionTimeout(30);
        
        // MIME mappings
//        for (int i = 0; i < Tomcat.DEFAULT_MIME_MAPPINGS.length;) {
//            ctx.addMimeMapping(DEFAULT_MIME_MAPPINGS[i++],
//                    DEFAULT_MIME_MAPPINGS[i++]);
//        }
        
        // Welcome files
        ctx.addWelcomeFile("index.html");
        ctx.addWelcomeFile("index.htm");
        ctx.addWelcomeFile("index.jsp");
		
		try {
			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void newT() throws LifecycleException
	{
		int startPort = 8888;
		int shutdownPort = 9999;
		//设置工作目录
        String catalina_home = new File("").getAbsolutePath() ;
        String appBase = catalina_home +"/webapps";
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(startPort);
        //设置工作目录,其实没什么用,tomcat需要使用这个目录进行写一些东西
        tomcat.setBaseDir(catalina_home);
        
      //设置程序的目录信息
        tomcat.getHost().setAppBase(appBase);
        // Add AprLifecycleListener
        StandardServer server = (StandardServer) tomcat.getServer();
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
        //注册关闭端口以进行关闭
        tomcat.getServer().setPort(shutdownPort);
        
      //加载上下文
//        StandardContext standardContext = (StandardContext)tomcat.addContext("/", appBase);
        StandardContext standardContext = new StandardContext();
        standardContext.setPath("/");//contextPath
        standardContext.setDocBase(appBase);//文件目录位置
        standardContext.addLifecycleListener(new Tomcat.DefaultWebXmlListener());//默认解析
        //保证已经配置好了。
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());
        standardContext.setSessionCookieName("t-session");
        //是指绑定servlet
        TestServlet testServer = new TestServlet();
        String name = testServer.getHttpServletName();
		String pattern = testServer.getPattern();
		Tomcat.addServlet(standardContext,name,testServer);
		standardContext.addServletMapping(pattern,name);
        
        
        tomcat.getHost().addChild(standardContext);
        
        tomcat.start();
        tomcat.getServer().await();
	}
	
	public static void test() throws LifecycleException
	{
		int startPort = 8888;
		int shutdownPort = 9999;
		//设置工作目录
        String catalina_home = /*"d:/"*/new File("").getAbsolutePath() ;
        String appBase = catalina_home +"/webapps";
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(startPort);
        //设置工作目录,其实没什么用,tomcat需要使用这个目录进行写一些东西
        tomcat.setBaseDir(catalina_home);
        
      //设置程序的目录信息
        tomcat.getHost().setAppBase(appBase);
        // Add AprLifecycleListener
        StandardServer server = (StandardServer) tomcat.getServer();
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
        //注册关闭端口以进行关闭
        tomcat.getServer().setPort(shutdownPort);
        
      //加载上下文
        StandardContext standardContext = new StandardContext();
        standardContext.setPath("/");//contextPath
        standardContext.setDocBase(appBase);//文件目录位置
        standardContext.addLifecycleListener(new Tomcat.DefaultWebXmlListener());//默认解析
        //保证已经配置好了。
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());
        standardContext.setSessionCookieName("t-session");
        //是指绑定servlet
        TestServlet testServer = new TestServlet();
        String name = testServer.getHttpServletName();
		String pattern = testServer.getPattern();
		Tomcat.addServlet(standardContext,name,testServer);
		standardContext.addServletMapping(pattern,name);
        
        
        tomcat.getHost().addChild(standardContext);
        
        tomcat.start();
        tomcat.getServer().await();
	}
	
	
	
}
