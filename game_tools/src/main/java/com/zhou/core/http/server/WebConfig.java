package  com.zhou.core.http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.config.ToolsConfig;
	/**
	 * Gm 参数配置类,通过key 获取value值
	 */
public class WebConfig {
	private static final Logger logger = LogManager.getLogger(WebConfig.class);
	static Properties properties =new Properties();
	public static void loadProperties() {
		Properties temp = new Properties();
		String fileName = WebConfig.class.getClassLoader().getResource(ToolsConfig.WEB_CONFIG_FILE_NAME).getPath();
		try
		{
			
			logger.info("<<<<<<<<<<<<<load properties:"+fileName);
			File file = new File(fileName);
			temp.load(new FileInputStream(file));
			properties = temp;
			logger.info("Game Properties loaded!");
		}
		catch (Exception e)
		{
			logger.error("ERROR IN LOAD "+fileName, e);
		}
	}
	
	public static String getString(String key,String default_value) 
	{
		String value = default_value;
		try {
			String t = properties.getProperty(key);
			if (t == null || t.isEmpty()) return value;
			value = t;
		} catch (Exception e) {
			logger.error("getString Key["+key+"] is error,please check configuration in "+ToolsConfig.GM_CONFIG_FILE_NAME,e);
		}
		return value;
	}
	
	public static int getInt(String key,int default_value) 
	{
		int value = default_value;
		try {
			String t = properties.getProperty(key);
			if (t == null || t.isEmpty()) return value;
			value = Integer.valueOf(t);
		} catch (Exception e) {
			logger.error("getInt Key["+key+"] is error,please check configuration in "+ToolsConfig.GM_CONFIG_FILE_NAME,e);
		}
		return value;
	}
	
	public static String[] getStringArrays(String key,String regex) 
	{
		try {
			String t = properties.getProperty(key);
			if (t == null || t.isEmpty()) return null;
			return t.split(regex);
		} catch (Exception e) {
			logger.error("getStringArrays Key["+key+"] is error,please check configuration in "+ToolsConfig.GM_CONFIG_FILE_NAME,e);
			return null;
		}
	}
	
	public void unload() {
		PrintWriter pw = null;
		try {
			String fileName = WebConfig.class.getClassLoader().getResource(ToolsConfig.WEB_CONFIG_FILE_NAME).getPath();
			logger.info("<<<<<<<<<<<<<unload properties:"+fileName);
			File file = new File(fileName);
			pw = new PrintWriter(file);
			for (Entry<Object,Object> entry : properties.entrySet()) {
				pw.println(entry.getKey()+ " = " + (entry.getValue() == null ? "" : entry.getValue()));
			}
			pw.flush();
			
		} catch (Throwable e) {
			logger.error(e);
		}finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	
	public Properties getProperties() {
		return properties;
	}

	public static String getUnicodeString(String name) {
		StringBuffer sb = new StringBuffer();
		for (char c : name.toCharArray()) {
			sb.append("\\u"+Integer.toHexString(c));
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		String name =" ";
		try {
			System.out.println(new String(name.getBytes("unicode"), "unicode"));
			for (char c : name.toCharArray()) {
				System.out.print("\\u"+Integer.toHexString(c));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
