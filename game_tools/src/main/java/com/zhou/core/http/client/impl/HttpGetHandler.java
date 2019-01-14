package com.zhou.core.http.client.impl;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.http.client.HttpClientHandler;
/**
 * @description http post请求处理类
 * @author zhouyongjun
 *
 */
public class HttpGetHandler extends AbstractHttpClientHandler{
	private static final Logger logger = LogManager.getLogger(HttpGetHandler.class);

	int counnection_timeout = 5000;
	int so_timeout = 10000;
	public HttpGetHandler() {
	}
	
	public HttpGetHandler(int counnection_timeout, int so_timeout) {
		this.counnection_timeout = counnection_timeout;
		this.so_timeout = so_timeout;
	}
	
	
	public String handle(String url,NameValuePair... params) throws Exception
	{
		HttpClient client = HttpClients.createDefault();
		
		StringBuffer  extension_url = new StringBuffer(url); 
		if (params != null && params.length > 0)
		{
			if (url.contains("?"))
			{
				extension_url.append("&");
			}
			else
			{	
				extension_url.append("?");				
			}
			for (NameValuePair pair : params)
			{
				extension_url.append(pair.getName()).append("=").append(pair.getValue()).append("&");
			}
			extension_url.deleteCharAt(extension_url.length()-1);
		}
		logger.info("http request:" + extension_url.toString());
		HttpGet get = new HttpGet(extension_url.toString());
		//TODO 链接配置
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, counnection_timeout);		
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, so_timeout);
		HttpResponse response = client.execute(get);
//		System.err.println(response.getStatusLine());
		
		HttpEntity respEntity = response.getEntity();
		String result =  null;
		if (respEntity != null)
		{
			result = EntityUtils.toString(respEntity,"UTF-8");
		}
//		System.err.println("response entitiy:"+result);
		logger.info("http response: status="+response.getStatusLine()+"\n "+result);
		return result;
	}
}
