package com.zhou.study.dbcp2;

public class DBConfig {
	public static final String POOL_ACTION_FAIL = "FAIL";

	public static final String POOL_ACTION_BLOCK = "BLOCK";
	public static final String POOL_ACTION_GROW = "GROW";
	public boolean active = false;
	public String driverName ;
	public String connectionString;
	public String userName ;
	public String password;
	public String testSql;
	public int maxActiveConnections = 10;
	public int maxIdleConnections = 10;
	public String exhaustedPoolAction = "FAIL";
	public int blockTime = 3000;

	public DBConfig() {
		this.driverName = "com.mysql.jdbc.Driver";
		this.connectionString = "";
		this.userName = "root";
		this.password = "lt.123";
		this.testSql = "select now();";
	}
}
