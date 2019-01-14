package com.zhou.core.flow;

import org.slf4j.Logger;

/**
 * 流量控制接口
 * @author zhouyongjun
 *
 * @param <T>
 * @param <V>
 */
public interface IFlowControl<T,V> {
	/**
	 * 流量记录
	 * @param key 单个对象的统计
	 * @param value 单个类型的统计
	 * @param isNeedOxHex value 是否显示为16进制
	 * @param bytes 流量字节数
	 */
	public void record(T key,V value,boolean isNeedOxHex,int bytes);
	/**
	 * 清空数据
	 */
	public void clear();
	/**
	 * 日更新
	 */
	public void daily(Logger logger);
	/**
	 * 写日志
	 */
	public String writeLog(Logger logger);
	/**
	 * 流量ID设置
	 * @param id
	 */
	public void setFlowId(String id);
	/**
	 * 
	 * @return
	 */
	public String getFlowId();
}
