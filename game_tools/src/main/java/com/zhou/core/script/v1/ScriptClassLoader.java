package com.zhou.core.script.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
	/**
	 * 脚本自定义加载器
	 * @author zhouyongjun
	 * @deprecated
	 * @param <T>
	 */
public class ScriptClassLoader<T> extends ClassLoader{
	private final static Logger logger = LogManager.getLogger(ScriptClassLoader.class);
	public static String SCRIPT_LOAD_PATH = "script/";
	
	
	@SuppressWarnings("unchecked")
	public T loadScript(String name)throws Exception {
		String fileName = SCRIPT_LOAD_PATH + name;
		String end = ".class";
		if (!fileName.endsWith(end)) {fileName = fileName + end;}
		Class<?> c = loadClass(new File(fileName));
		return (T)c.newInstance();
	}
	public Class<?> loadClass(File file) throws ClassNotFoundException {
		InputStream in = null;
		try {
			logger.info("file["+file.getName()+"]"+file.getAbsolutePath()+" ");
			in = new FileInputStream(file);
			byte[] bytes = new byte[10240000];
			int limit = in.read(bytes);
			return defineClass(null,bytes,0, limit);
		} catch (Exception e) {
			logger.error("find class["+file.getName()+"] is error...",e);
			return null;
		}finally {
			if(in != null)
				try {in.close();} catch (IOException e) {}
		}
	}

	
	public static void main(String[] args) {
		System.out.println(SCRIPT_LOAD_PATH);
	}
	
}
