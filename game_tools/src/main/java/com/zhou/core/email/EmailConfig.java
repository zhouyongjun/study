package com.zhou.core.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zhou.config.ToolsConfig;
import com.zhou.util.StringUtils;
import com.zhou.util.XmlUtils;
/**
 * 后台配置
 * @author zhouyongjun
 *
 */
public final class EmailConfig {
	public static boolean EMAIL_TICK_SWITCH = true;//邮件发送开关
	public static boolean EMAIL_SENDED_SWITCH = true;//邮件发送开关
	public static String EMAIL_SENDED_USER = ""; //发送者邮箱
	public static String EMAIL_SENDED_PSSWD= "";//发送者邮箱密码
	public static String EMAIL_SENDED_SERVER_HOST= "";//发送者邮箱服务器地址
	public static int 	 EMAIL_SENDED_PORT= 25;//
	public static byte XOR_KEY = 9;//异或加密key
	public static int scheduleAtFixedRate_second = 5;//间隔时间
	public static void load() throws Throwable
	{
		loadXml();
	}
	
	
	public static void parse() {
		EMAIL_SENDED_PSSWD = getXORString(EMAIL_SENDED_PSSWD);
	}


	
	public static String getUnicodeString(String name) {
		StringBuffer sb = new StringBuffer();
		for (char c : name.toCharArray()) {
			sb.append("\\u"+Integer.toHexString(c));
		}
		return sb.toString();
	}
	
	public static String getXORString(String src) {
		try {
			byte[] bytes = src.getBytes("utf-8");
			byte[] eor_bytes = new byte[bytes.length];
			for (int i=0;i<eor_bytes.length;i++) {
				eor_bytes[i] = (byte) (bytes[i]^XOR_KEY);
			}
			return new String(eor_bytes,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private static final Logger logger = LogManager.getLogger(EmailConfig.class);
	
	public static void loadXml()throws Throwable{
		String path = EmailConfig.class.getClassLoader().getResource(ToolsConfig.EMAIL_CONFIG_FILE_NAME).getPath();
		logger.info("<<<<<<<<<<<<<load file:"+path);
		Document doc  = XmlUtils.load(path);
		Element rootE = doc.getDocumentElement();
		scheduleAtFixedRate_second= StringUtils.getInt(rootE.getAttribute("scheduleAtFixedRate_second"));
		EMAIL_TICK_SWITCH= StringUtils.isBoolean(rootE.getAttribute("tick_switch"));
		EMAIL_SENDED_SWITCH = StringUtils.isBoolean(rootE.getAttribute("sended_switch"));
		EMAIL_SENDED_USER = rootE.getAttribute("sended_user");
		EMAIL_SENDED_PSSWD = getXORString(rootE.getAttribute("sended_passwd"));
		EMAIL_SENDED_SERVER_HOST = rootE.getAttribute("sended_server_host");
		EMAIL_SENDED_PORT = Integer.parseInt(rootE.getAttribute("sended_server_port"));
		Element[] els = XmlUtils.getChildrenByName(rootE, "email");
		Map<String,EmailListener> temps = new HashMap<>();
		for (Element e : els) 
		{
			loadEmailListener(e,temps);
		}
		EmailService.getInstance().stopListeners();
		for (Entry<String,EmailListener> entry : temps.entrySet())
		{
			EmailService.getInstance().registListener(entry.getKey(), entry.getValue());
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	private static void loadEmailListener(Element e,Map<String, EmailListener> temps) throws Exception {
		String type = e.getAttribute("type");
		Class<? extends EmailListener> clz = (Class<? extends EmailListener>) Class.forName(e.getAttribute("class_name"));
		EmailListener listener = clz.newInstance();
		listener.setType(type);
		listener.load(e);
		temps.put(type.toLowerCase(), listener);
	}


	public static void main(String[] args) {
//		String name =" ";
//		try {
//			System.out.println(new String(name.getBytes("unicode"), "unicode"));
//			for (char c : name.toCharArray()) {
//				System.out.print("\\u"+Integer.toHexString(c));
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		System.err.println(getXORString("e}8?:;98?"));
		System.err.println(getXORString("el}hgn8;:"));
	}
}
