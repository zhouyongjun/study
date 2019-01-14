package com.zhou;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.config.ToolsConfig;
import com.zhou.core.telnet.TelnetService;
import com.zhou.core.telnet.impl.DefaultTelnetService;
	/**
	 * 控制台入口类
	 * @author zhouyongjun
	 *
	 */
public final class ToolsApp {
	
	private final static Logger logger = LogManager.getLogger(ToolsApp.class);
	private final static ToolsApp instance = new ToolsApp();
	
	private TelnetService telnetService = new DefaultTelnetService();
	/*
	 * 判断是console.properties是否已经加载过
	 * 考虑到：使用者会单独使用某个子系统，而不走ConsoleApp.startup()方法
	 * 所以若为false，则直接调用子系统会加载一次
	 */
	private boolean isConfigLoad;
	private ToolsApp() {
		init();
	}
	
	private void init() {
		try {
			showVersionInfo();
		} catch (Throwable e) {
			logger.error("ConsoleApp init is error...",e);
		}
	}

	public static ToolsApp getInstance() {
		return instance;
	}
	private void showVersionInfo(){
		/**
		 * 0.0.1-SNAPSHOT 版本提供telnet后台命令
		 */
		/*logger.info("---------------------------------------------------");
		logger.info("************game.tools.jar 0.0.1-SNAPSHOT************");
		logger.info("		子系统：提供后台telnet接入，支持命令输入 ConsoleApp.getInstance().getTelnetService().startup() 启动入口和startConsole 启动telnet处理命令");
		logger.info("************game.tools.jar 0.1.1-SNAPSHOT************");
		logger.info("		子系统：提供后台脚本加载class文件命令 : ");
		logger.info("      		1).class文件存在script目录下， ");
		logger.info("      		2).格式：文件名|参数|参数...");
		logger.info("************game.tools.jar 0.1.2-SNAPSHOT************");
		logger.info("		子系统：提供定时器线程池 TaskPoolService.getInstance().startup() 启动入口");
		logger.info("************game.tools.jar 0.1.3-SNAPSHOT************");
		logger.info("		子系统：提供白名单 WhitelistService.getInstance().startup() 启动入口");
		logger.info("************game.tools.jar 1.0.1-SNAPSHOT************");
		logger.info("		子系统：发送内容到指定电子邮件Email EmailService.getInstance().startup() 启动入口");
		logger.info("		子系统：web功能 new TomcatServer().startup() 启动入口");
		logger.info("		子系统：集成gm server端功能 GmServer.getInstance().startup() 启动入口 ");
		logger.info("		子系统：集成gm client端功能 GmClient.getInstance().startup() 启动入口");
		logger.info("		子系统：xml write功能封装  XmlWriter入口类");
		logger.info("		线程池启动调整：默认启动，无限外部调用启动 ");
		logger.info("		telnet目录结构调整 ");
		logger.info("************ game.tools.jar 1.0.2 ************");
		logger.info("		子系统：系统信息查看服务，OSService 调入入口");
		logger.info("		邮件功能大调整：①支持大的类型配置，②支持条件过滤配置，③支持发送过滤配置④支持配置大类型发送给不同邮箱⑤支持jar外部扩展");
		logger.info("		子系统：文件增加、删除自动检测 VFSService.getInstance().startup() 启动入口，可配置自动检测目录或文件，可配置触发文件事件");
		logger.info("************ game.tools.jar 1.0.3 ************");
		logger.info("		BUG:解决CPU无法检测进程ID<5位的情况");
		logger.info("		时间事件增加listener对象，通过load方法注入，Key为listener");
		logger.info("************ game.tools.jar 1.0.4 ************");
		logger.info("		子系统：增加web客户端增加get,post,upload,download等功能");
		logger.info("		优化：web服务端Tomcat启动参数,扩张Servlet颗粒化服务");
		logger.info("************ game.tools.jar 1.0.5 ************");
		logger.info("		子系统 ：热加载增加加载jar和自动加载功能<script.jar,script.auto>");*/
//		logger.info("************ game.tools.jar 1.0.6 ************");
//		logger.info("		子系统 ：流量统计,服务类：FlowControlService");
		logger.info("************ game.tools.jar 1.0.7 ************");
		logger.info("		子系统：增加web支持JSP功能");
		logger.info("		子系统：增加web版本GM后台： ①支持开新服务器操作，②支持维护操作");
		logger.info("		子系统：ISFSObject 一套存储内存对象 KEY-VALUE 格式工具");
		logger.info("		子系统：手机短信发送（接 【中国网建】http://www.webchinese.com.cn/）");
		logger.info("---------------------------------------------------");
	}
	
	public enum SystemType
	{
		TELENT,
		SCRIPT,
		TASK_POOL,
		WHITE_LIST,
		EMAIL,
		HTTP_WEB,
		GM_SERVER,
		GM_CLIENT,
		VFS,
		;
	};
	
	public interface ConsoleSystem {
		SystemType getConsoleSubSystemType();
		public void startup(Object... params) throws Throwable;
		public void shutdown(Object... params) throws Throwable;
		boolean isStartUp();
	}

	public void loadConfigPropeties() throws Throwable {
		ToolsConfig.loadProperties();
	}
	
	public boolean isConfigLoaded() 
	{
		return isConfigLoad;
	}

	public TelnetService getTelnetService() {
		return telnetService;
	}

	public void setTelnetService(TelnetService telnetService) {
		this.telnetService = telnetService;
	}
	
}


