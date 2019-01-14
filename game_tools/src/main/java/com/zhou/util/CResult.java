package com.zhou.util;

/**
 * ����ֵ��
 * @author zhouyongjun
 *
 */
public final class CResult {
	public static final CResult  RESULT_SUCC = new CResult((byte)0);
	public static final CResult  RESULT_FAIL = new CResult((byte)1);
	
	private byte result;//0:�ɹ���1ʧ��
	private String msg;
	private CParam data;
	/**
	 * 
	 * @param result 0:�ɹ���1ʧ��
	 */
	private CResult(byte result)
	{
		this.result = result;
	}
	
	/**
	 * ����ʧ�ܽ�������
	 * @param msg: ʧ����Ϣ
	 * @return
	 */
	public static  CResult createFailResult(String msg)
	{
		return createFailResult(msg, null);
	}
	
	/**
	 * ����ʧ�ܽ�������
	 * @param msg: ʧ����Ϣ
	 * @param data: ���ش��Ĳ�����Ϣ
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
	 *  * �����ɹ���������
	 * @param msg: ʧ����Ϣ
	 * @return
	 */
	public static  CResult createSuccResult(String msg)
	{
		return createFailResult(msg, null);
	}
	
	/**
	* �����ɹ���������
	 * @param msg: ʧ����Ϣ
	 * @param data: ���ش��Ĳ�����Ϣ
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
	 * @return 0:�ɹ���1:ʧ��
	 */
	public byte getResult() {
		return result;
	}
	
	/**
	 * �������
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
	 * ��ȡ����
	 * @param key
	 * @return ��������ݶ���
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
