package com.zhou.core.gm.bs.servlet.control;

import java.util.ArrayList;
import java.util.List;

public enum ServerParam {
	IP(0,"ip","�·���ַ:","47.89.209.190"),
	PATH(0,"path","�·�·��:","/data/admin/aok/1102_server"),
	SERVER_NAME(0,"server_name","��������:","����ţ��"),
	INSTANCE_ID(1,"instance_id","ʵ����:","0x60EB"),
	SERVER_ID(1,"server_id","���������:","1102"),
	SERVER_VERSON(1,"server_verson","�������汾:","1002"),
	RMI_PORT(1,"rmi_port","RMI�˿ں�:","7100"),//RMI�˿ں�
	REDIS_LOCAL_IP(1,"redis_local_ip","REDIS-LOCAL��ַ:","127.0.0.1"),//REDIS-LOCAL��ַ
	REDIS_LOCAL_PORT(1,"redis_local_port","REDIS-LOCAL�˿ں�:","6381"),//REDIS-LOCAL�˿ں�
	REDIS_LOCAL_DB(1,"redis_local_db","REDIS-LOCAL���ݿ�:","0"),//REDIS-LOCAL���ݿ�
	SQL_CMD_DIR(1,"sql_cmd_dir","SQL����Ŀ¼:","/opt/mysql/bin/"),//SQL����Ŀ¼
	SQL_PORT(1,"sql_port","SQL�˿ں�:","3306"),//SQL�˿ں�
	SQL_NAME(1,"sql_name","SQL��¼�˺�:","game"),//SQL��¼�˺�
	SQL_PASSWD(1,"sql_passwd","SQL��¼����(����):","game@2015.run"),//SQL��¼����(����)
	SQL_DB(1,"sql_db","���ݿ�����:","aok_1102"),//���ݿ�����
	REDIS_GLOBAL_IP(1,"redis_global_ip","REDIS-GLOBAL��ַ:","10.28.217.48"),//REDIS-GLOBAL��ַ
	REDIS_GLOBAL_PROT(1,"redis_global_port","REDIS-GLOBAL�˿ں�:","6381"),//REDIS-GLOBAL�˿ں�
	REDIS_GLOBAL_DB(1,"redis_global_db","REDIS-GLOBAL���ݿ�:","0"),//REDIS-GLOBAL���ݿ�
	SSH_PORT(1,"shh_port","SSH�˿ں�:","22022"),//SSH�˿ں�
	SSH_USER_NAME(1,"ssh_user_name","SSH��¼�û���:","aok"),//SSH��¼�û���
	SSH_PASSWD(1,"ssh_paswd","SSH��¼����(����):","w2H3XD28TT"),//SSH��¼����(����)
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
