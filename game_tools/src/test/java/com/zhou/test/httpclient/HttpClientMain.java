package com.zhou.test.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zhou.core.http.client.HttpClientService;
import com.zhou.core.http.client.impl.HttpDownloadHandler;
import com.zhou.core.http.client.impl.HttpPostHandler;

public class HttpClientMain {
	public static void main(String[] args) {
//		testUpload();
//		testPost();
		testGet();
//		testDownload();
	}
	private static void testDownload() {
		try {
			new HttpDownloadHandler().handle("http://localhost:8081/game/download"
					, new BasicNameValuePair("filename","zkclient-0.1.jar"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void testGet() {
		try {
//			new HttpPostHandler().handle("http://localhost:8081/tt/default"
//					,new BasicNameValuePair("playerId", "2986039_001_0")
//					,new BasicNameValuePair("instanceId", "7938"));
			new HttpPostHandler()
			.handle("http://www.hfss.gov.cn/tmp/news_liuyan.shtml?m_id=1172&SS_ID=61");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static void testUpload() {
		try {
			List<NameValuePair> paramters = new ArrayList<NameValuePair>();
			paramters.add(new BasicNameValuePair("/jar", "zkclient-0.1.jar"));
			paramters.add(new BasicNameValuePair("/image", "dongman1109.png"));
			HttpClientService.doUpload("http://localhost:8081/game/upload", paramters.toArray(new NameValuePair[0]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private static void testPost() {
		try {
			new HttpPostHandler().handle("http://localhost:9900/pay/select-player"
					,new BasicNameValuePair("playerId", "2986039_001_0")
					,new BasicNameValuePair("instanceId", "7938"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
