package com.zhou.core.http.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.Container;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.http.server.servlet.AbstractServlet;
import com.zhou.exception.ConsoleException;
/**
 * tomcat 容器封装
 * @author zhouyongjun
 *
 */
public final class TomcatContext {
	private static final Logger logger = LogManager.getLogger(TomcatContext.class);
	StandardContext context;
	//小程序列表
	Map<String,AbstractServlet> servlets;
	State state = State.INIT;
	public TomcatContext(StandardContext context) {
		this.context = context;
		this.servlets = new HashMap<>();
	}
	/**
	 * 激活
	 * @param tomcat
	 * @throws Exception 
	 */
	public void start() throws Exception{
		logger.info("  Tomcat@context["+this.getClass().getName()+"] start.");
//		if (servlets.isEmpty())
//		{
//			throw new ConsoleException("Map<String,AbstractServlet> is empty,please register AbstractServlet Object before start,error.");
//		}
		if (state == State.INIT || state == State.DESTORY)
		{
			for (AbstractServlet servlet : servlets.values())
			{
				configServlet(servlet);
			}
		}
		state = State.START;
	}
	
	/**
	 * 停止
	 */
	public void stop() throws ConsoleException {
		try {
			if (state != State.START) 
			{
				throw new ConsoleException("Tomcat Context["+this.getClass().getName()+"] state is not State.START.");
			}
			state = State.STOP;
			logger.info("   Tomcat@context["+this.getClass().getName()+"] stop.");
		} catch (Exception e) {
			logger.error("context["+this.getClass().getName()+"] name["+this.getContext().getPath()+"] stop is error.",e);
		}
	}
	
	/**
	 * 销毁
	 */
	public void destory() throws ConsoleException {
		try {
			if (state == State.DESTORY) 
			{
				logger.error("Tomcat Context["+this.getClass().getName()+"] is destoryed before.");
				return;
			}
			try {
				stop();
				} catch (ConsoleException e) {}
			state = State.DESTORY;
			logger.info("   Tomcat@context["+this.getClass().getName()+"] destory.");
			for (AbstractServlet servlet : new ArrayList<>(servlets.values())) {
				destoryHttpServlet(servlet.getHttpServletName(), servlet.getPattern());
			}
			servlets.clear();
		} catch (Exception e) {
			logger.error("context["+this.getClass().getName()+"] name["+this.getContext().getPath()+"] stop is error.",e);
		}
	}
	
	/**
	 * 配置servlet
	 * @param servlet
	 */
	private void configServlet(AbstractServlet servlet) {
		String name = servlet.getHttpServletName();
		String pattern = servlet.getPattern();
		Tomcat.addServlet(context,name,servlet);
		context.addServletMapping(pattern,name);
		context.setSessionCookieName(name+"-session");
		logger.debug("   	Tomcat@AbstractServlet["+servlet.getClass().getName()+"] http servlet name["+name+"],pattern["+pattern+"] config succ.");
	}
	
	/**
	 * 激活小程序
	 * @throws ConsoleException 
	 */
	public void startServlet(AbstractServlet servlet) throws Exception {
		if (state != State.START)
		{
			throw new ConsoleException("state != State.INIT, error.");
		}
		logger.debug("   Tomcat@context["+this.getClass()+"] activateServlet.");
		addServlet(servlet);
		configServlet(servlet);
	}
	
	/**
	 * 注册一个HttpServelt
	 * @param servlet
	 * @throws ConsoleException 
	 */
	public void registerServlet(AbstractServlet servlet) throws Exception {
		if (state != State.INIT)
		{
			throw new ConsoleException("state != State.INIT, error.");
		}
		addServlet(servlet);
	}
	
	private void addServlet(AbstractServlet servlet) throws ConsoleException {
		String name = servlet.getHttpServletName();
//		if (servlets.containsKey(name))
//		{
//			throw new ConsoleException("AbstractServlet["+servlet+"] getHttpServletName["+name+"] is exist,error.");
//		}
		servlets.put(name, servlet);
		logger.debug("Context[{}] registerServlet[{},{}"+"] http servlet name={} put map succ.",getContext().getPath(),servlet.getPattern(),servlet.getClass().getName(),name);
	}
	
	/**
	 * 摧毁一个servlet
	 * @param servletName
	 * @param parttern
	 */
	public void destoryHttpServlet(String servletName,String parttern) {
		Container container = context.findChild(servletName);
		logger.debug("   Tomcat@AbstractServlet["+(container == null ? "null" : container.getClass().getName())+"] http servlet name["+servletName+"],pattern["+parttern+"] destory succ.");
		if (container != null) context.removeChild(container);
		context.removeServletMapping(parttern);
	}
	
	
	public StandardContext getContext() {
		return context;
	}
	public void setContext(StandardContext context) {
		this.context = context;
	}
	
	public AbstractServlet getServlet(String httpServletName) {
		return servlets.get(httpServletName);
	}
	
	public Map<String,AbstractServlet> getServlets() {
		return servlets;
	}
	
	public void setServlets(Map<String,AbstractServlet> servlets) {
		this.servlets = servlets;
	}
}
