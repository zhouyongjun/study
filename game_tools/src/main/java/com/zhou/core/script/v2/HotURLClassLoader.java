package com.zhou.core.script.v2;

import java.net.URL;
import java.net.URLClassLoader;
	/**
	 * 热加载，实现URLClassLoader
	 * ①主要加载 jar文件，提供jar文件入口类
	 * ②加载class文件
	 * @author zhouyongjun
	 *
	 */
public class HotURLClassLoader extends URLClassLoader{

	public HotURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
}
