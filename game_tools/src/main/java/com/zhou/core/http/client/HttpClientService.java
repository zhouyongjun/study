package com.zhou.core.http.client;

import java.util.Map;

import org.apache.http.NameValuePair;

import com.zhou.core.http.client.impl.HttpUploadHandler;

public final class HttpClientService {
	
	public static void doUpload(String url,NameValuePair... paramters) throws Exception
	{
		new HttpUploadHandler().handle(url, paramters);
	}
}
