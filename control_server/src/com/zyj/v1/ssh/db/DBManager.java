package com.zyj.v1.ssh.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 数据库管理
 * 
 * @author mabin
 * 
 */
public class DBManager {
	private static DBManager dbManager = null;
	private ApplicationContext context = null;
	private GameDAO gameDao = null;
	private ServerListDAO serverListDao = null;

	public static DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	public void init() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		gameDao = (GameDAO) context.getBean("gameDao");
		serverListDao = (ServerListDAO) context.getBean("serverlistDao");
	}
	
	public GameDAO getGameDao() {
		return gameDao;
	}

	public ServerListDAO getServerListDao() {
		return serverListDao;
	}

}
