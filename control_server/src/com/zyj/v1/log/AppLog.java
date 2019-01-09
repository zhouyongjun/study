package com.zyj.v1.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyj.v1.ssh.Server;


public class AppLog {
	public static final char SPLIT_CHAR = '|';
	

	private final static Logger logger = LoggerFactory.getLogger(AppLog.class);

	public static void error(String error) {
		logger.error(error);
	}
	public static void error(Server server,String error) {
		logger.error(server +error);
	}

	public static void error(Server server,String error, Throwable e) {
		logger.error(server +error, e);
	}
	
	public static void error(String error, Throwable e) {
		logger.error(error, e);
	}

	public static void error(Throwable e) {
		logger.error("error", e);
	}
	public static void error(Server server,Throwable e) {
		logger.error(server + "error", e);
	}

	public static void info(String info) {
		logger.info(info);
	}
	public static void info(Server server,String info) {
		logger.info(server +info);
	}

	public static void debug(String deb) {
		logger.debug(deb);
	}

	public static void warn(String warn) {
		logger.warn(warn);
	}
	public static void main(String[] args) {
		info("...................");
	}
}
