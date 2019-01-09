package com.zyj.v1.ssh.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.zyj.v1.log.AppLog;

/**
 * 链接指定服务器的数据库JDBC操作
 * @author zhouyongjun
 *
 */

public class JdbcDao {
	Connection conn;
	Statement statement;
	 // 加载驱动程序
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			AppLog.error("JdbcDao Class.forName(com.mysql.jdbc.Driver) is error...",e);
		}		
	}
	public boolean connect(String ip,int port,String dbName,String user,String password) {
		 String url =String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8",ip,port,dbName);
		 try { 
	         conn = DriverManager.getConnection(url, user, password);
	         statement = conn.createStatement();
	         if(conn.isClosed()) { 
	        	 return false;
	         }
	         return true;
		 }catch (Exception e) {
			 AppLog.error("jdbc connect["+url+","+user+","+password+"] is errror...",e);
			 return false;
		}
	}
	
	public boolean deleteDatabase(String dbName){
		return executeCmd("drop database "+dbName);
	}
	
	public boolean createDatabase(String dbName){
		return executeCmd("create database "+dbName);
	}
	
	public boolean executeCmd(String sql) {
		try {
			statement.execute(sql);
			return true;
		} catch (SQLException e) {
//			AppLog.error("jdbc executeCmd:"+sql+" is error...",e);
			return false;
		}
	}
	
	public boolean close() {
		try {
			if (statement != null) {
				statement.close();				
			}
			if (conn != null) {
				conn.close();				
			}
		} catch (Exception e) {
			AppLog.error("jdbc close is error...",e);
		}
		return true;
	}
}
