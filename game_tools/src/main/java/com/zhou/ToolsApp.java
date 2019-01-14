package com.zhou;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.config.ToolsConfig;
import com.zhou.core.telnet.TelnetService;
import com.zhou.core.telnet.impl.DefaultTelnetService;
	/**
	 * ����̨�����
	 * @author zhouyongjun
	 *
	 */
public final class ToolsApp {
	
	private final static Logger logger = LogManager.getLogger(ToolsApp.class);
	private final static ToolsApp instance = new ToolsApp();
	
	private TelnetService telnetService = new DefaultTelnetService();
	/*
	 * �ж���console.properties�Ƿ��Ѿ����ع�
	 * ���ǵ���ʹ���߻ᵥ��ʹ��ĳ����ϵͳ��������ConsoleApp.startup()����
	 * ������Ϊfalse����ֱ�ӵ�����ϵͳ�����һ��
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
		 * 0.0.1-SNAPSHOT �汾�ṩtelnet��̨����
		 */
		/*logger.info("---------------------------------------------------");
		logger.info("************game.tools.jar 0.0.1-SNAPSHOT************");
		logger.info("		��ϵͳ���ṩ��̨telnet���룬֧���������� ConsoleApp.getInstance().getTelnetService().startup() ������ں�startConsole ����telnet��������");
		logger.info("************game.tools.jar 0.1.1-SNAPSHOT************");
		logger.info("		��ϵͳ���ṩ��̨�ű�����class�ļ����� : ");
		logger.info("      		1).class�ļ�����scriptĿ¼�£� ");
		logger.info("      		2).��ʽ���ļ���|����|����...");
		logger.info("************game.tools.jar 0.1.2-SNAPSHOT************");
		logger.info("		��ϵͳ���ṩ��ʱ���̳߳� TaskPoolService.getInstance().startup() �������");
		logger.info("************game.tools.jar 0.1.3-SNAPSHOT************");
		logger.info("		��ϵͳ���ṩ������ WhitelistService.getInstance().startup() �������");
		logger.info("************game.tools.jar 1.0.1-SNAPSHOT************");
		logger.info("		��ϵͳ���������ݵ�ָ�������ʼ�Email EmailService.getInstance().startup() �������");
		logger.info("		��ϵͳ��web���� new TomcatServer().startup() �������");
		logger.info("		��ϵͳ������gm server�˹��� GmServer.getInstance().startup() ������� ");
		logger.info("		��ϵͳ������gm client�˹��� GmClient.getInstance().startup() �������");
		logger.info("		��ϵͳ��xml write���ܷ�װ  XmlWriter�����");
		logger.info("		�̳߳�����������Ĭ�������������ⲿ�������� ");
		logger.info("		telnetĿ¼�ṹ���� ");
		logger.info("************ game.tools.jar 1.0.2 ************");
		logger.info("		��ϵͳ��ϵͳ��Ϣ�鿴����OSService �������");
		logger.info("		�ʼ����ܴ��������֧�ִ���������ã���֧�������������ã���֧�ַ��͹������â�֧�����ô����ͷ��͸���ͬ�����֧��jar�ⲿ��չ");
		logger.info("		��ϵͳ���ļ����ӡ�ɾ���Զ���� VFSService.getInstance().startup() ������ڣ��������Զ����Ŀ¼���ļ��������ô����ļ��¼�");
		logger.info("************ game.tools.jar 1.0.3 ************");
		logger.info("		BUG:���CPU�޷�������ID<5λ�����");
		logger.info("		ʱ���¼�����listener����ͨ��load����ע�룬KeyΪlistener");
		logger.info("************ game.tools.jar 1.0.4 ************");
		logger.info("		��ϵͳ������web�ͻ�������get,post,upload,download�ȹ���");
		logger.info("		�Ż���web�����Tomcat��������,����Servlet����������");
		logger.info("************ game.tools.jar 1.0.5 ************");
		logger.info("		��ϵͳ ���ȼ������Ӽ���jar���Զ����ع���<script.jar,script.auto>");*/
//		logger.info("************ game.tools.jar 1.0.6 ************");
//		logger.info("		��ϵͳ ������ͳ��,�����ࣺFlowControlService");
		logger.info("************ game.tools.jar 1.0.7 ************");
		logger.info("		��ϵͳ������web֧��JSP����");
		logger.info("		��ϵͳ������web�汾GM��̨�� ��֧�ֿ��·�������������֧��ά������");
		logger.info("		��ϵͳ��ISFSObject һ�״洢�ڴ���� KEY-VALUE ��ʽ����");
		logger.info("		��ϵͳ���ֻ����ŷ��ͣ��� ���й�������http://www.webchinese.com.cn/��");
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


