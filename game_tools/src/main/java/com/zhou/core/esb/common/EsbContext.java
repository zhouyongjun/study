package com.zhou.core.esb.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.zhou.core.esb.exception.EsbException;
/**
 * ESB»·¾³
 * @author zhouyongjun
 *
 */
public class EsbContext {
	Properties property = new Properties();
	
	public void loadProperties(String fileName) throws EsbException
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
			property.load(br);
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if (br == null)
				try {br.close();} catch (IOException e) {}
		}
	}

	public Properties getProperties() {
		return property;
	}

	
//	public Object get(Object key)
//	{
//		return property.get(key);
//		
//	}
//	
	@SuppressWarnings("unchecked")
	public <T> T get(Object key)
	{
		return (T) property.get(key);
	}
	
	
	public <T> void put(String key, T value)
	{
		property.put(key, value);
	}
	
	
	public static void main(String[] args) {
		EsbContext context = new EsbContext();
		context.put("ss", "sfsf");
		context.put("ss", 11);
		String str = context.get("sss");
		System.out.print(str+"");
	}
	
}
