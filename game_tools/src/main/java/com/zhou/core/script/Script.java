package com.zhou.core.script;

import java.util.List;
	/**
	 * �ű��ӿ�
	 * @author zhouyongjun
	 *
	 */
public interface Script {
	/**
	 * ִ�з���
	 * @param outs
	 * @param params
	 * @throws Exception
	 */
	public void run(List<String> outs,String... params) throws Exception;
	
	/**
	 * ֹͣ�����script
	 * @param outs
	 * @param params
	 * @throws Exception
	 */
	public void stop(List<String> outs,String... params) throws Exception;
	/**
	 * �Ƿ񻺴浽�ڴ���
	 * @return
	 */
	public boolean isNeedCache();
}
