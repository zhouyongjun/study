package com.zhou.util;

import java.util.Properties;

/**
 * ����������
 * @author zhouyongjun
 *
 */
public class CParam {
	private Properties data;
	
	public CParam() {
		
	}
	
	public CParam(Properties data) {
		this.data = data;
	}
	
	public static CParam newInstance()
	{
		return new CParam();
	}
	
	public CParam put(Object key,Object value)
	{
		if (data == null)
		{
			data = new Properties();
		}
		data.put(key, value);
		return this;
	}
	/**
	 * ��ȡT����ֵ
	 * @param key : keyֵ
	 * @param clz ����Ҫ�ķ��ؽ����class����
	 * @return T 
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key,Class<T> clz)
	{
		return (T) get(key);
	}
	
	/**
	 * ��ȡObject���󷵻���
	 * @param key
	 * @return
	 */
	public Object get(Object key)
	{
		if (data == null) return null;
		if (!containKey(key))return null;
		return data.get(key);
	}

	public Properties getData() {
		return data;
	}
	
	
	public boolean containKey(Object key)
	{
		return data.containsKey(key);
	}
}
