package com.zhou.core.sms;

import java.util.HashMap;
import java.util.Map;

public class SMSConfig {
	
	public static String SEND_URL = "http://utf8.api.smschinese.cn/";//Gbk����Url	http://gbk.api.smschinese.cn/ , Utf-8����Url	http://utf8.api.smschinese.cn/
	public static String GET_URL = "http://www.smschinese.cn/web_api/SMS/?Action=SMS_Num";//&Uid=��վ�û���&Key=�ӿڰ�ȫ��Կ
	public static String CHARSET= "utf-8";
	public static String UID = "superman110";//��վ�û���
	public static String KEY = "d41d8cd98f00b204e980";
	public static String KeyMD5 = "";//����Ҫ���ܲ��������Key�������ĳ�KeyMD5��KeyMD5=�ӿ���Կ32λMD5���ܣ���д��
	public static int ONCE_PHONE_DAY_MAX_SEND_NUM = 20;//ͬһ��IP�绰 1�췢�������
	public static int SEND_CD_TIME_SECOND = 60;//���
	public static Map<Integer,String> errorCode = new HashMap<>(); 
	{
		String codeStr = "-1:û�и��û��˻�|-2:�ӿ���Կ����ȷ [�鿴��Կ],�����˻���½����|-21:MD5�ӿ���Կ���ܲ���ȷ|-3:������������|-11:���û�������|-14:�������ݳ��ַǷ��ַ�|-4:�ֻ��Ÿ�ʽ����ȷ|-41:�ֻ�����Ϊ��|-42:��������Ϊ��|-51:����ǩ����ʽ����ȷ,�ӿ�ǩ����ʽΪ����ǩ�����ݡ�|-52:����ǩ��̫��,����ǩ��10���ַ�����|-6:IP����";
		for (String s1 : codeStr.split("\\|"))
		{
			String[] s2 = s1.split(":");
			if (s2.length < 2) continue;
			errorCode.put(Integer.parseInt(s2[0]), s2[0]);
		}
	}
	
}
