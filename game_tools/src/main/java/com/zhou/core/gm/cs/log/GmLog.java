package  com.zhou.core.gm.cs.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * @author gejing
 * 
 */
public class GmLog {
	private static Logger logger = LogManager.getLogger(GmLog.class);
	public static void registerLogger(Logger log) {
		logger = log;
	}
	public static void error(String error) {
		if (logger == null) {
			println(error);	
		}else {
			logger.error(error);
		}
	}
	
	private static void println(String msg) {
		System.out.println(msg);
	}
	private static void println(String msg,Throwable throwable) {
		System.out.println(msg+throwable);
	}
	
	public static void error(String error, Throwable throwable) {
		if (logger == null) {
			println(error,throwable);
		}else {
			logger.error(error, throwable);
		}
	
	}

	public static void error(Throwable throwable) {
		if (logger == null) {
			println("error",throwable);	
		}else {
			logger.error("error", throwable);
		}
	}

	public static void info(String info) {
		if (logger == null) {
			println(info);	
		}else {
			logger.info(info);
		}
	
	}

	public static void debug(String deb) {
		if (logger == null) {
			println(deb);	
		}else {
			logger.debug(deb);
		}
	
	}

	public static void warn(String warn) {
		if (logger == null) {
			println(warn);	
		}else {
			logger.warn(warn);
		}
	}
}
