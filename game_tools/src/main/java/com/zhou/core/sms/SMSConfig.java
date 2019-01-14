package com.zhou.core.sms;

import java.util.HashMap;
import java.util.Map;

public class SMSConfig {
	
	public static String SEND_URL = "http://utf8.api.smschinese.cn/";//Gbk编码Url	http://gbk.api.smschinese.cn/ , Utf-8编码Url	http://utf8.api.smschinese.cn/
	public static String GET_URL = "http://www.smschinese.cn/web_api/SMS/?Action=SMS_Num";//&Uid=本站用户名&Key=接口安全秘钥
	public static String CHARSET= "utf-8";
	public static String UID = "superman110";//本站用户名
	public static String KEY = "d41d8cd98f00b204e980";
	public static String KeyMD5 = "";//如需要加密参数，请把Key变量名改成KeyMD5，KeyMD5=接口秘钥32位MD5加密，大写。
	public static int ONCE_PHONE_DAY_MAX_SEND_NUM = 20;//同一个IP电话 1天发送最多量
	public static int SEND_CD_TIME_SECOND = 60;//间隔
	public static Map<Integer,String> errorCode = new HashMap<>(); 
	{
		String codeStr = "-1:没有该用户账户|-2:接口密钥不正确 [查看密钥],不是账户登陆密码|-21:MD5接口密钥加密不正确|-3:短信数量不足|-11:该用户被禁用|-14:短信内容出现非法字符|-4:手机号格式不正确|-41:手机号码为空|-42:短信内容为空|-51:短信签名格式不正确,接口签名格式为：【签名内容】|-52:短信签名太长,建议签名10个字符以内|-6:IP限制";
		for (String s1 : codeStr.split("\\|"))
		{
			String[] s2 = s1.split(":");
			if (s2.length < 2) continue;
			errorCode.put(Integer.parseInt(s2[0]), s2[0]);
		}
	}
	
}
