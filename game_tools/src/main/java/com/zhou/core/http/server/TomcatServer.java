package com.zhou.core.http.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.ToolsApp;
import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.core.http.server.servlet.AbstractServlet;
import com.zhou.core.task.DefaultScheduledPool;
import com.zhou.core.task.Taskable;
import com.zhou.exception.ConsoleException;
/**
 * tomcat ����
 * @author zhouyongjun
 *
 */
public final class TomcatServer implements ConsoleSystem {
	protected Tomcat tomcat = new Tomcat();
	protected Map<String,TomcatContext> contexts = new HashMap<>();
	protected State state = State.INIT;
	private final static Logger logger = LogManager.getLogger(TomcatServer.class);
	protected ScheduledFuture<?> future = null;
	protected String project_path = null;
	protected String app_base = null;
	public void startup(Object... params) throws Throwable
	{
		logger.info("TomcatServer startup.");
//		if (contexts.isEmpty())
//		{
//			throw new ConsoleException("List<TomcatContext> is empty,please register TomcatContext Object before startup,error.");
//		}
		if (!ToolsApp.getInstance().isConfigLoaded())
		{
			ToolsApp.getInstance().loadConfigPropeties();
		}
		WebConfig.loadProperties();
		project_path = new File("").getAbsolutePath();
		app_base =project_path+WebConfig.getString("http.app.base.dir", "/webapps");
		int post = WebConfig.getInt("http.app.post", 9100);
		if (params != null && params.length > 0 && params[0] instanceof Integer)
		{
			post += (Integer)params[0];
		}
		logger.info("  Tomcat@project_path :"+project_path);
		logger.info("  Tomcat@app_base :"+app_base);
		logger.info("  Tomcat@port :"+post);
		if (state == State.INIT || state == State.DESTORY)
		{
			//���ö˿ں�
			tomcat.setPort(post);
			//������Ŀ·��
			tomcat.setBaseDir(project_path);
			//����web����Ӧ��·��
			tomcat.getHost().setAppBase(app_base);
			//��������
			tomcat.getConnector().setProperty("maxKeepAliveRequests", WebConfig.getString("maxKeepAliveRequests", "10"));
			tomcat.getConnector().setProperty("soTimeout",WebConfig.getString("soTimeout", "20000"));
			tomcat.getConnector().setProperty("keepAliveout", WebConfig.getString("keepAliveout", "50000"));
			logger.info("  Tomcat@@connetor properties:maxKeepAliveRequests="
					+ tomcat.getConnector().getProperty("maxKeepAliveRequests")
					+", soTimeout="+tomcat.getConnector().getProperty("soTimeout")
					+", keepAliveout="+tomcat.getConnector().getProperty("keepAliveout"));
		}
		//�������������б�
		logger.info("  Tomcat@context size: "+contexts.size());
		for (TomcatContext context : new ArrayList<>(contexts.values()))
		{
			configTomcatContext(context);
//			logger.info("  Tomcat@@context Config : "+context.getClass().getSimpleName());
		}
		
		tomcat.getServer().addLifecycleListener(new AprLifecycleListener());
	
		//����
		tomcat.start();
		state = State.START;
		
		//����ѭ���ȴ�
		if (future != null) future.cancel(true);
		future = DefaultScheduledPool.getInstance().schedule(new Taskable() {
			
			@Override
			public void run() {
				try {
					tomcat.getServer().await();
				} catch (Exception e) {
					logger.error("TOMCAT SERVER[PORT="+tomcat.getServer().getPort()+"] await error! ",e);
				}
			}
		});
		
	}
	/**
	 * ����context
	 * @param context
	 * @throws Exception 
	 */
	private void configTomcatContext(TomcatContext context) throws Exception
	{
		logger.info("   Tomcat@context["+context.getClass().getName()+"] config succ.");
		if (state == State.INIT || state == State.DESTORY) 
		{
			String context_path = context.getContext().getDocBase();
			File file = new File(tomcat.getHost().getAppBase()+"/"+context_path);
			if (!file.exists()) file.mkdirs();
		}
		context.start();		
	}
	
	/**
	 * ֹͣ
	 * @throws Exception
	 */
	public void stop(Object... params) throws Exception
	{
		if (state != State.START) 
		{
			throw new ConsoleException("Tomcat Server["+this.getClass().getName()+"] state is not State.START.");
		}
		state = State.STOP;
		logger.info("Tomcat Server["+this.getClass().getName()+"] stop.");
		for (TomcatContext context : new ArrayList<>(contexts.values())) {
			context.stop();
		}
		tomcat.stop();
	}
	/**
	 * ����
	 * @throws Exception
	 */
	public void destory() throws Exception
	{
		if (state == State.DESTORY) 
		{
			logger.error("Tomcat Server["+this.getClass().getName()+"] is destoryed before.");
			return;
		}
		try {
			stop();
			} catch (ConsoleException e) {}
		state = State.DESTORY;
		logger.info("Tomcat Server["+this.getClass().getName()+"] stop.");
		for (TomcatContext context : new ArrayList<>(contexts.values())) {
			context.destory();
		}
		tomcat.destroy();
		contexts.clear();
	}
	
	/**
	 * 
	 * @param contextPath �����������appBase·��,Ŀ¼�����ڻᴴ��һ��Ŀ¼
	 * @param docBase �ļ����Ŀ¼��,��������ڣ���������ʱ��ᱨ�������Զ�����Ŀ¼
	 * @param servlet... һ�������Ŀ����ж��servlet������url�е�·����Ψһ��
	 * @return TomcatContext��װ�õ��������� 
	 * @throws Exception
	 * @description ˵����
	 eg:http://localhost:9900/pay/select-player?instanceId=7938&playerId=2986040_001_0
	 url�߼����������֣�˳�����£�
		1)�������ߵ�ַ:�˿ں� : localhost:9900
		2)contextPath��������·�� : /pay
		3)HttpServlet getPattern():servletƥ����߼�����·��Ψһ�� /select-player  
	   
		HttpServlet ��Ӧweb���ù�ϵ��
			<servlet>
			 	<servlet-name>ListFileServlet</servlet-name> -- ��ӦhttpServlet.getName()
				<servlet-class>me.gacl.web.controller.ListFileServlet</servlet-class> -- ��ӦhttpServletʵ�����ȫ·����
			</servlet>
			<servlet-mapping>
				<servlet-name>ListFileServlet</servlet-name>  -- ��ӦhttpServlet.getName()
				<url-pattern>/servlet/ListFileServlet</url-pattern>  -- ��ӦhttpServlet.getPattern()
			</servlet-mapping> 
		<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			@author zhouyongjun 
			@date 	2018-05-02
			@descr:
				�޸�֧��JSP
					1.ȥ�� (StandardContext)tomcat.addContext(contextPath, docBase);
						 ֱ�� new StandardContext();����
						ע�⣺DefaultWebXmlListener ����
						���tomcat.getHost().addChild(standardContext);
					2.docBase ��ҪΪ����·�������ߴη�����ת�ɾ���·�� 
	 */
	public TomcatContext registerTomcatContext(String contextPath,String docBase,AbstractServlet... servlets) throws Exception {

//		StandardContext context = (StandardContext)tomcat.addContext(contextPath, docBase);
		
		StandardContext standardContext = new StandardContext();
        standardContext.setPath(contextPath);//contextPath
        standardContext.setDocBase(docBase);//�ļ�Ŀ¼λ��
        standardContext.addLifecycleListener(new Tomcat.DefaultWebXmlListener());//Ĭ�Ͻ���
        //��֤�Ѿ����ú��ˡ�
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());
//        standardContext.setSessionCookieName("t-session");
		
        //��֤�Ѿ����ú��ˡ�
		TomcatContext tomcatContext= registerTomcatContext(new TomcatContext(standardContext));
		if (servlets != null && servlets.length > 0)
		{
			for (AbstractServlet servlet : servlets)
			{
				if (servlet == null) continue;
				tomcatContext.registerServlet(servlet);	
			}
		}
		
		tomcat.getHost().addChild(standardContext);
		return tomcatContext;
	}
	/**
	 * ע��context����
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private TomcatContext registerTomcatContext(TomcatContext context) throws Exception 
	{
		if (state != State.INIT)
		{
			throw new ConsoleException("state is not State.INIT, error.");
		}
		addTomcatContext(context);
		return context;
		
	}
	/**
	 * list���
	 * @param context
	 */
	private void addTomcatContext(TomcatContext context) 
	{
		contexts.put(context.getContext().getPath(), context);
	}
	/**
	 * ��̬����tomcat����
	 * @param context
	 * @throws Exception 
	 */
	public void startTomcatContext(TomcatContext context) throws Exception 
	{
		if (state != State.START)
		{
			throw new ConsoleException("state is not State.START, error.");
		}
		configTomcatContext(context);
		addTomcatContext(context);
	}
	
	/**
	 * ��̬ɾ��tomcat����
	 * @param context
	 * @throws ConsoleException 
	 */
	public void stopTomcatContext(TomcatContext context) throws Exception 
	{
		if (state != State.START)
		{
			throw new ConsoleException("state is not State.START, error.");
		}
		if (context == null) return;
		context.stop();
	}
	
	/**
	 * ��̬ɾ��tomcat����
	 * @param context
	 * @throws ConsoleException 
	 */
	public void destoryTomcatContext(TomcatContext context) throws Exception 
	{
		if (state != State.START)
		{
			throw new ConsoleException("state is not State.START, error.");
		}
		if (context == null) return;
		context.destory();
		contexts.remove(context);
		tomcat.getHost().removeChild(context.getContext());
	}
	public Tomcat getTomcat() {
		return tomcat;
	}
	
	public Map<String,TomcatContext> getContexts() {
		return contexts;
	}
	
	public TomcatContext getContext(String contextPath) {
		return contexts.get(contextPath);
	}
	
	public State getState() {
		return state;
	}
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.HTTP_WEB;
	}
	@Override
	public void shutdown(Object... params) throws Throwable {
		destory();
		if (future != null)
		{
			future.cancel(true);			
		}
	}


	public ScheduledFuture<?> getFuture() {
		return future;
	}
	@Override
	public boolean isStartUp() {
		return state == State.START;
	}
	
	public String getApp_base() {
		return app_base;
	}
	
	public String getProject_path() {
		return project_path;
	}
}
