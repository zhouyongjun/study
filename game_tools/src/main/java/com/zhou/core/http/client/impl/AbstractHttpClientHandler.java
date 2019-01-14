package com.zhou.core.http.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zhou.core.http.client.HttpClientHandler;

public abstract class AbstractHttpClientHandler implements HttpClientHandler {
	
	public AbstractHttpClientHandler() {
	}
	
	@Override
	public String handle(String url, Map<String, String> params)
			throws Exception {
		List<NameValuePair> pairs = new ArrayList<>();
		for (Entry<String,String> entry : params.entrySet())
		{
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return handle(url, pairs.toArray(new NameValuePair[0]));
	}
}
