package com.zhou.core.http.client;

import java.util.Map;

import org.apache.http.NameValuePair;
/**
 * http�ͻ�����������
 * @author zhouyongjun
 *
 */
public interface HttpClientHandler {
	String handle(String url,NameValuePair... params) throws Exception;
	String handle(String url,Map<String,String> params) throws Exception;
}
