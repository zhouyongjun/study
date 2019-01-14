package com.zhou.util;

/**
 * 返回值类
 * @author zhouyongjun
 *
 */
public final class CResult {
	public static final CResult  RESULT_SUCC = new CResult((byte)0);
	public static final CResult  RESULT_FAIL = new CResult((byte)1);
	
	private byte result;//0:成功，1失败
	private String msg;
	private CParam data;
	/**
	 * 
	 * @param result 0:成功，1失败
	 */
	private CResult(byte result)
	{
		this.result = result;
	}
	
	/**
	 * 创建失败结果类对象
	 * @param msg: 失败信息
	 * @return
	 */
	public static  CResult createFailResult(String msg)
	{
		return createFailResult(msg, null);
	}
	
	/**
	 * 创建失败结果类对象
	 * @param msg: 失败信息
	 * @param data: 返回带的参数信息
	 * @return
	 */
	public static  CResult createFailResult(String msg,CParam data)
	{
		CResult result = new CResult((byte)1);
		result.msg = msg;
		result.data = data;
		return result;
	}
	
	/**
	 *  * 创建成功结果类对象
	 * @param msg: 失败信息
	 * @return
	 */
	public static  CResult createSuccResult(String msg)
	{
		return createFailResult(msg, null);
	}
	
	/**
	* 创建成功结果类对象
	 * @param msg: 失败信息
	 * @param data: 返回带的参数信息
	 * @return
	 */
	public static  CResult createSuccResult(String msg,CParam data)
	{
		CResult result = new CResult((byte)0);
		result.msg = msg;
		result.data = data;
		return result;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public CParam getData() {
		return data;
	}


	public void setData(CParam data) {
		this.data = data;
	}

	/**
	 * 
	 * @return 0:成功，1:失败
	 */
	public byte getResult() {
		return result;
	}
	
	/**
	 * 添加数据
	 * @param <T>
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> CResult putData(Object key,T value)
	{
		if (data == null) data = new CParam();
		data.put(key, value);
		return this;
	}
	/**
	 * 获取数据
	 * @param key
	 * @return 保存的数据对象
	 */
	public <T> T getData(Object key,Class<T> clz)
	{
		if (data == null) return null;
		return (T) data.get(key, clz);
	}
	
	
	public Object getData(Object key)
	{
		if (data == null) return null;
		return data.get(key);
	}
	
	public boolean isSucc()
	{
		return result == RESULT_SUCC.result;
	}
	
	public boolean isFail()
	{
		return result == RESULT_FAIL.result;
	}
}
