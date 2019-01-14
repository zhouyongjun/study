package com.zhou.test.redis;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertyFileReader {
	static Properties properties;
	public static void load() {
		properties = new Properties();
		try {
			properties.load(PropertyFileReader.class.getClassLoader().getResourceAsStream("redis.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static int getInt(String key, int defaultValue) {
		String value = getString(key);
		return StringUtils.isNotBlank(value) ? Integer.parseInt(value) : defaultValue;
	}

	public static int getInt(String key) {
		return getInt(key,0);
	}


	public static long getLong(String key) {
		String value = getString(key);
		return StringUtils.isNotBlank(value) ? Long.parseLong(value) : 0;
	}

	public static boolean getBooolean(String key) {
		String value = getString(key);
		return StringUtils.isNotBlank(value) && value.equals("1");
	}

}
