package com.zhou.core.http.client.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.http.client.HttpClientHandler;
/**
 * @description  post请求处理类
 * @author zhouyongjun
 *
 */
public class HttpPostHandler extends AbstractHttpClientHandler{
	private static final Logger logger = LogManager.getLogger(HttpPostHandler.class);
	
	int counnection_timeout = -1;//ms
	int so_timeout = -1;//ms
	public HttpPostHandler() {
	}
	
	public HttpPostHandler(int counnection_timeout, int so_timeout) {
		this.counnection_timeout = counnection_timeout;
		this.so_timeout = so_timeout;
	}

	@SuppressWarnings("deprecation")
	public String handle(String url,NameValuePair... params) throws Exception
	{
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		if (params != null && params.length > 0)
		{
			parameters.addAll(Arrays.asList(params));
		}
		logger.info("http request:" + url);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);
		post.setEntity(entity);
		//TODO 链接配置
		post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, counnection_timeout);		
		post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, so_timeout);
		HttpResponse response = client.execute(post);
		HttpEntity respEntity = response.getEntity();
		String result =  null;
		if (respEntity != null)
		{
			result = EntityUtils.toString(respEntity,"UTF-8");
		}
		logger.info("http response: status="+response.getStatusLine()+"\n "+result);
		return result;
	}
	
}
