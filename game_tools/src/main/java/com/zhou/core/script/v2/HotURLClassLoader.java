package com.zhou.core.script.v2;

import java.net.URL;
import java.net.URLClassLoader;
	/**
	 * �ȼ��أ�ʵ��URLClassLoader
	 * ����Ҫ���� jar�ļ����ṩjar�ļ������
	 * �ڼ���class�ļ�
	 * @author zhouyongjun
	 *
	 */
public class HotURLClassLoader extends URLClassLoader{

	public HotURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
}
