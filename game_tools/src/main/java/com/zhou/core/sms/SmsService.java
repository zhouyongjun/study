package com.zhou.core.sms;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.zhou.config.ToolsConfig;
import com.zhou.core.http.client.impl.HttpGetHandler;
import com.zhou.util.ConfigPropertiesLoader;
import com.zhou.util.JsonUtil;
import com.zhou.util.StringUtils;

/**
 * ���ŷ���
 * @author zhouyongjun
 * 
 * 
 * ������֤��1����ֻ�ܵ������1�Σ�
	��ͬIP�ֻ�����1������ύ20�Σ�
	��֤����ŵ����ֻ�����30��������ύ10�Σ�
	���ύҳ�����ͼ��У���룬��ֹ�����˶��ⷢ�ͣ�
	�ڷ�����֤��ӿڳ����У��ж�ͼ��У���������Ƿ���ȷ��
	���û��ýӿڲ�����֤��ʱ���������룺���Ե��޹�������Ϣ����ֱ�����룺��֤��:xxxxxx�����͡�
 *
 */
public class SmsService {
	ConfigPropertiesLoader config;
	static SmsService instance = new SmsService();
	
	private SmsService() {
	}
	
	public static SmsService getInstance() {
		return instance;
	}
	
	public ConfigPropertiesLoader getConfig() {
		return config;
	}
	
	public void startup()
	{
		config = new ConfigPropertiesLoader(ToolsConfig.SMS_FILE_NAME);
		config.loadProperties();
		parseErrorCode();
	}
	public void parseErrorCode()
	{
		for (String s1 : config.getString("sms.sended.error.code","").split("\\|"))
		{
			String[] s2 = s1.split(":");
			if (s2.length < 2) continue;
			errorCode.put(s2[0], s2[1]);
		}
	}
	
	
	public String getUID()
	{
		return config.getString("sms.uid", "");
	}
	
	public String getKEY()
	{
		return config.getString("sms.key", "");
	}
	HashMap<String, String> errorCode = new HashMap<String, String>();
	
	
	public String sendSms(String text,String... phones)
	{

		try {
			String smsMob = StringUtils.join(phones, ",");
			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(config.getString("sms.sended.url",""));
			post.addHeader("Content-Type","application/x-www-form-urlencoded;charset="+config.getString("sms.charset","UTF-8"));
			NameValuePair[] data = 
					{ 	new BasicNameValuePair("Uid", getUID()),
						new BasicNameValuePair("Key", getKEY()),
						new BasicNameValuePair("smsMob", smsMob),
						new BasicNameValuePair("smsText", text) 
					};
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data));
			post.setEntity(entity);
			//TODO ��������
			HttpResponse response = client.execute(post);
			HttpEntity respEntity = response.getEntity();
			String result =  null;
			HashMap<String,String> rm = new HashMap<>();
			if (respEntity != null)
			{
				result = EntityUtils.toString(respEntity,"UTF-8");
				rm.put("code", result);
				rm.put("errorMsg", errorCode.get(result));
			}
			
			return JsonUtil.toJson(rm);
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	
	}
	
	public String getSms()
	{
		NameValuePair[] data = { new BasicNameValuePair("Uid", getUID())
		,new BasicNameValuePair("Key",getKEY())};
		try {
			return new HttpGetHandler().handle(config.getString("sms.get.url",""), data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static void main(String[] args) {
		SmsService.getInstance().startup();
		System.out.println(SmsService.getInstance().getSms());
		SmsService.getInstance().sendSms("��֤��:8888", "18855126063");
	}

}
