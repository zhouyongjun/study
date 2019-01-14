package com.zhou.core.gm.bs.servlet.control;

import java.util.ArrayList;
import java.util.List;

public enum ServerParam {
	IP(0,"ip","新服地址:","47.89.209.190"),
	PATH(0,"path","新服路径:","/data/admin/aok/1102_server"),
	SERVER_NAME(0,"server_name","服务器名:","工具牛逼"),
	INSTANCE_ID(1,"instance_id","实例号:","0x60EB"),
	SERVER_ID(1,"server_id","服务器编号:","1102"),
	SERVER_VERSON(1,"server_verson","服务器版本:","1002"),
	RMI_PORT(1,"rmi_port","RMI端口号:","7100"),//RMI端口号
	REDIS_LOCAL_IP(1,"redis_local_ip","REDIS-LOCAL地址:","127.0.0.1"),//REDIS-LOCAL地址
	REDIS_LOCAL_PORT(1,"redis_local_port","REDIS-LOCAL端口号:","6381"),//REDIS-LOCAL端口号
	REDIS_LOCAL_DB(1,"redis_local_db","REDIS-LOCAL数据库:","0"),//REDIS-LOCAL数据库
	SQL_CMD_DIR(1,"sql_cmd_dir","SQL命令目录:","/opt/mysql/bin/"),//SQL命令目录
	SQL_PORT(1,"sql_port","SQL端口号:","3306"),//SQL端口号
	SQL_NAME(1,"sql_name","SQL登录账号:","game"),//SQL登录账号
	SQL_PASSWD(1,"sql_passwd","SQL登录密码(加密):","game@2015.run"),//SQL登录密码(加密)
	SQL_DB(1,"sql_db","数据库名字:","aok_1102"),//数据库名字
	REDIS_GLOBAL_IP(1,"redis_global_ip","REDIS-GLOBAL地址:","10.28.217.48"),//REDIS-GLOBAL地址
	REDIS_GLOBAL_PROT(1,"redis_global_port","REDIS-GLOBAL端口号:","6381"),//REDIS-GLOBAL端口号
	REDIS_GLOBAL_DB(1,"redis_global_db","REDIS-GLOBAL数据库:","0"),//REDIS-GLOBAL数据库
	SSH_PORT(1,"shh_port","SSH端口号:","22022"),//SSH端口号
	SSH_USER_NAME(1,"ssh_user_name","SSH登录用户名:","aok"),//SSH登录用户名
	SSH_PASSWD(1,"ssh_paswd","SSH登录密码(加密):","w2H3XD28TT"),//SSH登录密码(加密)
	;
	int type;
	String paramName;
	String showName;
	String defaultValue;
	private ServerParam(int type,String paramName,String showName,String defaultValue) {
		this.type = type;
		this.paramName = paramName;
		this.showName = showName;
		this.defaultValue = defaultValue;
	}
	public static ServerParam[] getTableValues(int type)
	{
		List<ServerParam> list= new ArrayList<ServerParam>();
		for (ServerParam param : values())
		{
			if (param.type == type)
			{
				list.add(param);
			}
		}
		return list.toArray(new ServerParam[0]);
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public String getShowName() {
		return showName;
	}
	public int getType() {
		return type;
	}
	public String getParamName() {
		return paramName;
	}
	public static ServerParam getValueOfParam(String key) {
		for (ServerParam param : values())
		{
			if (param.paramName.equals(key))
			{
				return param;
			}
		}
		return null;
	}
}
