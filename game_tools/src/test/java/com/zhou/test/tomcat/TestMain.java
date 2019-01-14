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
		//����
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
		//���ù���Ŀ¼
        String catalina_home = new File("").getAbsolutePath() ;
        String appBase = catalina_home +"/webapps";
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(startPort);
        //���ù���Ŀ¼,��ʵûʲô��,tomcat��Ҫʹ�����Ŀ¼����дһЩ����
        tomcat.setBaseDir(catalina_home);
        
      //���ó����Ŀ¼��Ϣ
        tomcat.getHost().setAppBase(appBase);
        // Add AprLifecycleListener
        StandardServer server = (StandardServer) tomcat.getServer();
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
        //ע��رն˿��Խ��йر�
        tomcat.getServer().setPort(shutdownPort);
        
      //����������
//        StandardContext standardContext = (StandardContext)tomcat.addContext("/", appBase);
        StandardContext standardContext = new StandardContext();
        standardContext.setPath("/");//contextPath
        standardContext.setDocBase(appBase);//�ļ�Ŀ¼λ��
        standardContext.addLifecycleListener(new Tomcat.DefaultWebXmlListener());//Ĭ�Ͻ���
        //��֤�Ѿ����ú��ˡ�
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());
        standardContext.setSessionCookieName("t-session");
        //��ָ��servlet
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
		//���ù���Ŀ¼
        String catalina_home = /*"d:/"*/new File("").getAbsolutePath() ;
        String appBase = catalina_home +"/webapps";
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(startPort);
        //���ù���Ŀ¼,��ʵûʲô��,tomcat��Ҫʹ�����Ŀ¼����дһЩ����
        tomcat.setBaseDir(catalina_home);
        
      //���ó����Ŀ¼��Ϣ
        tomcat.getHost().setAppBase(appBase);
        // Add AprLifecycleListener
        StandardServer server = (StandardServer) tomcat.getServer();
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
        //ע��رն˿��Խ��йر�
        tomcat.getServer().setPort(shutdownPort);
        
      //����������
        StandardContext standardContext = new StandardContext();
        standardContext.setPath("/");//contextPath
        standardContext.setDocBase(appBase);//�ļ�Ŀ¼λ��
        standardContext.addLifecycleListener(new Tomcat.DefaultWebXmlListener());//Ĭ�Ͻ���
        //��֤�Ѿ����ú��ˡ�
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());
        standardContext.setSessionCookieName("t-session");
        //��ָ��servlet
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
