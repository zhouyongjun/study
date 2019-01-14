package com.zhou.test.sms;

import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class SMSMain {

	public static void main(String[] args) throws Exception {
		String url = "http://utf8.api.smschinese.cn/";
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf8");
		NameValuePair[] data = { new BasicNameValuePair("Uid", "superman110"),
				new BasicNameValuePair("Key", "d41d8cd98f00b204e980"),
				new BasicNameValuePair("smsMob", "18855126063"),
				new BasicNameValuePair("smsText", new String("��֤�� : 8888".getBytes())) };
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data));
		post.setEntity(entity);
		//TODO ��������
		HttpResponse response = client.execute(post);
		HttpEntity respEntity = response.getEntity();
		String result =  null;
		if (respEntity != null)
		{
			result = EntityUtils.toString(respEntity,"UTF-8");
		}
		System.out.println("result:"+result);
		
		
		/*HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// ��ͷ�ļ�������ת��
		NameValuePair[] data = { new NameValuePair("Uid", "��վ�û���"),
				new NameValuePair("Key", "�ӿڰ�ȫ��Կ"),
				new NameValuePair("smsMob", "�ֻ�����"),
				new NameValuePair("smsText", "��֤�룺8888") };
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		System.out.println(result); // ��ӡ������Ϣ״̬

		post.releaseConnection();*/

	}

}
