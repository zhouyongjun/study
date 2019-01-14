package com.zhou.core.script;

import java.util.List;
	/**
	 * 脚本接口
	 * @author zhouyongjun
	 *
	 */
public interface Script {
	/**
	 * 执行方法
	 * @param outs
	 * @param params
	 * @throws Exception
	 */
	public void run(List<String> outs,String... params) throws Exception;
	
	/**
	 * 停止缓存的script
	 * @param outs
	 * @param params
	 * @throws Exception
	 */
	public void stop(List<String> outs,String... params) throws Exception;
	/**
	 * 是否缓存到内存中
	 * @return
	 */
	public boolean isNeedCache();
}
