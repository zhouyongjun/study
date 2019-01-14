package com.zhou.core.script;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.core.script.v1.ScriptClassLoader;
import com.zhou.core.script.v2.HotURLClassLoader;
/**
 * 脚本加载服务
 * 单例模式
 * @author zhouyongjun
 *
 */
public final class ScriptService implements ConsoleSystem{
	private static final Logger logger = LogManager.getLogger(ScriptService.class);
	private static ScriptService service = new ScriptService();
	Map<String,Script> cacheScripts;
	protected boolean isStartUp;
	private ScriptService() {};
	
	public static ScriptService getInstance() {
		return service;
	}
	@Deprecated
	public void handleScript(String param,List<String> outs) {
		try {
			//加载器
			/*
			 * 同一个加载器不允许多次加载同一个class
			 */
			ScriptClassLoader<Script> classLoader = new ScriptClassLoader<Script>();
			String[] arrays = param.trim().split(" ");
			Script t = classLoader.loadScript(arrays[0]);
			String[] params = new String[arrays.length-1];
			if (params.length > 0) System.arraycopy(arrays, 1, params, 0, params.length);
			t.run(outs, params);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	
	
	public Map<String, Script> getCacheScripts() {
		return cacheScripts;
	}

	public void putCacheScript(String key,Script script) {
		if (cacheScripts == null)
		{
			setCacheScripts(new HashMap<String, Script>());
		}
		cacheScripts.put(key, script);
	}
	
	public Script getCacheScript(String key) {
		return cacheScripts != null && key != null ? cacheScripts.get(key) : null;
	}
	
	public void setCacheScripts(Map<String, Script> cacheScripts) {
		this.cacheScripts = cacheScripts;
	}

	@SuppressWarnings("resource")
	public void runScript(String param,List<String> outs)
	{
		try {
			//加载器
			/*
			 * 同一个加载器不允许多次加载同一个class
			 * 加载jar格式： class包路径+类名,jar名字  参数
			 * 加载class格式：class包路径+类名  参数
			 */
			ScriptConfig.loadProperties();
			String[] arrays = param.trim().split(" ");
			String clazzName = arrays[0];
			boolean isJar = clazzName.contains(":");
			String jarParentPath = null;
			String fileName = null;
			if (isJar)
			{
				String[] jarNames = arrays[0].split(":");
				clazzName = jarNames[0];
				jarParentPath = ScriptConfig.getString("script.jar.dir", "./script");
				if (!jarParentPath.endsWith("/")) jarParentPath += "/";
				fileName =jarParentPath + jarNames[1] + (jarNames[1].endsWith(".jar") ? "" : ".jar"); 
			}
			else
			{
				jarParentPath = ScriptConfig.getString("script.calzz.dir", "./script");
				fileName = jarParentPath;
			}
			
			URL[] urls = new URL[]{
					new File(fileName).toURI().toURL()
			};
			HotURLClassLoader classLoader = new HotURLClassLoader(urls, this.getClass().getClassLoader());
			String[] params = new String[arrays.length-1];
			if (params.length > 0) System.arraycopy(arrays, 1, params, 0, params.length);
			Script script = (Script) classLoader.loadClass(clazzName).newInstance();
			script.run(outs, params);
			if (script.isNeedCache())
			{
				Script cacheScript = getCacheScript(clazzName);
				stopScript(outs,cacheScript);
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
			outs.add(e.getMessage());
		}
	}
	
	private void stopScript(List<String> outs,Script cacheScript) throws Exception {
		if (cacheScript == null) return ;
		cacheScript.stop(outs);
	}

	@SuppressWarnings("resource")
	public void stopScript(String param,List<String> outs)
	{
		try {
			//加载器
			/*
			 * 同一个加载器不允许多次加载同一个class
			 * 加载jar格式： class包路径+类名,jar名字  参数
			 * 加载class格式：class包路径+类名  参数
			 */
			ScriptConfig.loadProperties();
			String[] arrays = param.trim().split(" ");
			stopScript(outs, getCacheScript(arrays[0]));	
			
		} catch (Throwable e) {
			logger.error(e.getMessage());
		}
	}
	
	
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.SCRIPT;
	}
	@Override
	public void startup(Object... params) throws Throwable {
		isStartUp = true;
	}
	@Override
	public void shutdown(Object... params) throws Throwable {
		isStartUp = false;
	}
	@Override
	public boolean isStartUp() {
		return isStartUp;
	}
}
