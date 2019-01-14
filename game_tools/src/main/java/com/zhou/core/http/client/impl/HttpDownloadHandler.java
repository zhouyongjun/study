package com.zhou.core.http.client.impl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.http.client.HttpClientHandler;
import com.zhou.exception.ConsoleException;
	/**
	 * @description 从web服务器下载资源类,一次只能下载一份文件
	 * 
	 * @author zhouyongjun
	 *
	 */
public class HttpDownloadHandler extends AbstractHttpClientHandler {
	private static final Logger logger = LogManager.getLogger(HttpDownloadHandler.class);
	
	int counnection_timeout = -1;
	int so_timeout = -1;
	public HttpDownloadHandler() {
	}
	
	public HttpDownloadHandler(int counnection_timeout, int so_timeout) {
		this.counnection_timeout = counnection_timeout;
		this.so_timeout = so_timeout;
	}
	/**
	 * @param url http路径
	 * @param pairs pairs[0] 指定web服务端资源文件名,pairs[1] 指定本地保存逻辑(可以不设置)
	 */
	@Override
	public String handle(String url, NameValuePair... pairs)
			throws Exception {
		if (pairs == null || pairs.length == 0) throw new ConsoleException("download filename need assign value.");
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		//TODO 链接配置
		post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, counnection_timeout);		
		post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, so_timeout);
		List<NameValuePair> parameters = Collections.singletonList(pairs[0]);
		String localFileName = pairs[0].getValue();
		if(pairs.length > 1) localFileName = pairs[1].getValue();
		post.setEntity(new UrlEncodedFormEntity(parameters));
		CloseableHttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		FileOutputStream out = new FileOutputStream(localFileName);
		InputStream in = entity.getContent();
		byte[] reads = new byte[1024];
		int length = 0;
		while(((length = in.read(reads))) > 0)
		{
			out.write(reads, 0, length);
			reads = new byte[1024];
			length = 0;
		}
		in.close();
		out.flush();
		out.close();
		return entity.getContentType().getName()+":"+entity.getContentType().getValue()+","+response.getStatusLine();
	}

}
