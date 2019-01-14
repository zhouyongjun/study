package com.zhou.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;
/**
 * 后台配置
 * @author zhouyongjun
 *
 */
public final class ToolsConfig {
	public static int TASK_POOL_SIZE = 5;//线程池线程数量上限
	public static boolean WHITE_LIST_SWTICH = false;//白名单
	public static String WHITE_LIST_FILE_NAME="whitelist.txt";
	public static String GM_CONFIG_FILE_NAME = "gm.properties";
	public static String EMAIL_CONFIG_FILE_NAME = "email.xml";
	public static String WEB_CONFIG_FILE_NAME = "web.properties";
	public static String VFS_CONFIG_FILE_NAME = "vfs.xml";
	public static String SCRIPT_FILE_NAME = "script.properties";
	public static String SMS_FILE_NAME = "sms.properties";
	public static String GEOIP2_CITY_FILE_NAME = "GeoLite2-City.mmdb";
	public static String GEOIP2_COUNTRY_FILE_NAME = "GeoLite2-Country.mmdb";
	
	
	public static void loadProperties()throws Throwable{
		Properties properties = new Properties();
//		String path = /*Config.class.getResource("/").getPath()+*/"./console.properties";
		URL url = ToolsConfig.class.getClassLoader().getResource("tools.properties");
		if (url == null) return;
		String path = ToolsConfig.class.getClassLoader().getResource("tools.properties").getPath();
		File file = new File(path);
		System.out.println("<<<<<<<<<<<<<load properties:"+file.getAbsolutePath());
		properties.load(new FileInputStream(file));

		for (Field f : ToolsConfig.class.getDeclaredFields())
		{
			String str = properties.getProperty(f.getName());
			if (str == null)
			{
				continue;
			}
			if (f.getType() == byte.class)
			{
				f.set(null, Byte.parseByte(properties.getProperty(f.getName(), f.get(null).toString().trim())));
			}
			else if (f.getType() == int.class)
			{
				f.set(null, Integer.parseInt(properties.getProperty(f.getName(), f.get(null).toString().trim())));
			}
			else if (f.getType() == short.class)
			{
				f.set(null, Short.parseShort(properties.getProperty(f.getName(), f.get(null).toString().trim())));
			}
			else if (f.getType() == float.class)
			{
				f.set(null, Float.parseFloat(properties.getProperty(f.getName(), f.get(null).toString().trim())));
			}
			else if (f.getType() == long.class)
			{
				f.set(null, Long.parseLong(properties.getProperty(f.getName(), f.get(null).toString().trim())));
			}
			else if (f.getType() == boolean.class)
			{
				f.set(null, Boolean.parseBoolean(properties.getProperty(f.getName(), f.get(null).toString().trim())));
			}
			else if (f.getType() == String.class)
			{
				f.set(null, properties.getProperty(f.getName(), f.get(null).toString().trim()));
			}
		}
	}
	

	public static void unload() throws Throwable{
		PrintWriter pw = null;
		try {
			String path = ToolsConfig.class.getResource("/").getPath()+"console.properties";
			System.out.println("<<<<<<<<<<<<<unload properties:"+path);
			File file = new File(path);
			pw = new PrintWriter(file);
			for (Field f : ToolsConfig.class.getDeclaredFields()) {
				if(f.getName().equals("SERVER_NAME")) {
					pw.println(f.getName() + "=" + getUnicodeString(f.get(null).toString()).trim());
				}else {
					pw.println(f.getName() + "=" + f.get(null).toString().trim());
				}
			}
			pw.flush();
			
		} catch (Throwable e) {
			throw e;
		}finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	public static String getUnicodeString(String name) {
		StringBuffer sb = new StringBuffer();
		for (char c : name.toCharArray()) {
			sb.append("\\u"+Integer.toHexString(c));
		}
		return sb.toString();
	}
	

}
