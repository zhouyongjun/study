package com.zhou.core.http.client.impl;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.http.client.HttpClientHandler;
/**
 * @description 负责上传到web服务端 资源文件类，可支持多个文件发送
 * @author zhouyongjun
 *
 */
public class HttpUploadHandler extends AbstractHttpClientHandler{
	private static final Logger logger = LogManager.getLogger(HttpUploadHandler.class);
	int counnection_timeout = -1;
	int so_timeout = -1;
	public HttpUploadHandler() {
	}
	
	public HttpUploadHandler(int counnection_timeout, int so_timeout) {
		this.counnection_timeout = counnection_timeout;
		this.so_timeout = so_timeout;
	}
	
	public String handle(String url,NameValuePair... paramters) throws Exception
	{
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(/*"http://localhost:8081/upload"*/url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.setCharset(Charset.forName("UTF-8"));
		for (NameValuePair pair : paramters)
		{
			builder.addBinaryBody(pair.getName(), new File(pair.getValue()));
		}
		HttpEntity entity  = builder.build();
//			HttpEntity entity = MultipartEntityBuilder.create()
//					.addBinaryBody("jar", new File("zkclient-0.1.jar"))
//					.addBinaryBody("image",  new File("dongman1109.png"))
//					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//					.setCharset(Charset.forName("UTF-8"))
//					.build();
		post.setEntity(entity);
		//TODO 链接配置
		post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, counnection_timeout);		
		post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, so_timeout);
		HttpResponse response = client.execute(post);
//		System.err.println(response.getStatusLine());
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
