package com.zhou.test.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void jUnitTest() {
		get();
	}

	/**
	 * HttpClient����SSL
	 */
	public void ssl() {
		CloseableHttpClient httpclient = null;
		try {
			
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));
			try {
				// ����keyStore d:\\tomcat.keystore  
				trustStore.load(instream, "123456".toCharArray());
			} catch (CertificateException e) {
				e.printStackTrace();
			} finally {
				try {
					instream.close();
				} catch (Exception ignore) {
				}
			}
			// �����Լ���CA��������ǩ����֤��
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
			// ֻ����ʹ��TLSv1Э��
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			// ����http����(get��ʽ)
			HttpGet httpget = new HttpGet("https://localhost:8443/myDemo/Ajax/serivceJ.action");
			System.out.println("executing request" + httpget.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
					System.out.println(EntityUtils.toString(entity));
					EntityUtils.consume(entity);
				}
			} finally {
				response.close();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * post��ʽ�ύ����ģ���û���¼����
	 */
	public void postForm() {
		// ����Ĭ�ϵ�httpClientʵ��.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// ����httppost  
		HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceJ.action");
		// ������������  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", "admin"));
		formparams.add(new BasicNameValuePair("password", "123456"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���� post������ʱ���Ӧ�ò����ݴ��ݲ�����ͬ���ز�ͬ���
	 */
	public void post() {
		// ����Ĭ�ϵ�httpClientʵ��.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// ����httppost  
		HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceJ.action");
		// ������������  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���� get����
	 */
	public void get() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// ����httpget.  
			HttpGet httpget = new HttpGet("http://www.baidu.com/");
			System.out.println("executing request " + httpget.getURI());
			// ִ��get����.  
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// ��ȡ��Ӧʵ��  
				HttpEntity entity = response.getEntity();
				System.out.println("--------------------------------------");
				// ��ӡ��Ӧ״̬  
				System.out.println(response.getStatusLine());
				if (entity != null) {
					// ��ӡ��Ӧ���ݳ���  
					System.out.println("Response content length: " + entity.getContentLength());
					// ��ӡ��Ӧ����  
					System.out.println("Response content: " + EntityUtils.toString(entity));
				}
				System.out.println("------------------------------------");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �ϴ��ļ�
	 */
	public void upload() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceFile.action");
			
			FileBody bin = new FileBody(new File("F:\\image\\sendpix0.jpg"));
			StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();

			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			postFile();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �ϴ��ļ� 
	 * @throws ParseException
	 * @throws IOException
	 */   
	public static void postFile() throws ParseException, IOException{
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    try {
	        // Ҫ�ϴ����ļ���·��
	        String filePath = new String("./zkclient-0.1.jar");
	        // ��һ����ͨ�������ļ��ϴ������������ַ ��һ��servlet
	        HttpPost httpPost = new HttpPost("http://localhost:8081/upload");
	        // ���ļ�ת����������FileBody
	        File file = new File(filePath);
	        FileBody bin = new FileBody(file);  
	        StringBody uploadFileName = new StringBody("zkclient-0.1.jar", ContentType.create("multipart/form-data", Consts.UTF_8));
	        //�����������ģʽ���У���ֹ�ļ������롣  
	           HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
	                .addPart("uploadFile", bin) //uploadFile��Ӧ��������ͬ������<File����>
//	                .addPart("uploadFileName", uploadFileName)//uploadFileName��Ӧ��������ͬ������<String����>
	                .setCharset(CharsetUtils.get("UTF-8")).build();
	 
	        httpPost.setEntity(reqEntity);
	        System.out.println("���������ҳ���ַ " + httpPost.getRequestLine()+"\t contentType: " + reqEntity.getContentType().getValue());
	        // �������� �������������Ӧ
	        CloseableHttpResponse response = httpClient.execute(httpPost);
	        try {
	            System.out.println("----------------------------------------");
	            // ��ӡ��Ӧ״̬
	            System.out.println(response.getStatusLine());
	            // ��ȡ��Ӧ����
	            HttpEntity resEntity = response.getEntity();
	            if (resEntity != null) {
	                // ��ӡ��Ӧ����
	                System.out.println("Response content length: "
	                        + resEntity.getContentLength());
	                // ��ӡ��Ӧ����
	                System.out.println(EntityUtils.toString(resEntity,
	                        Charset.forName("UTF-8")));
	            }
	            // ����
	            EntityUtils.consume(resEntity);
	        } finally {
	            response.close();
	        }
	    } finally {
	        httpClient.close();
	    }
	}
	 
	 /**
	 * �����ļ�
	 * @param url            http://www.xxx.com/img/333.jpg
	 * @param destFileName   xxx.jpg/xxx.png/xxx.txt
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void getFile(String url, String destFileName)
	        throws ClientProtocolException, IOException {
	    // ����һ��httpclient����
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	    HttpGet httpget = new HttpGet(url);
	    HttpResponse response = httpclient.execute(httpget);
	    HttpEntity entity = response.getEntity();
	    InputStream in = entity.getContent();
	    File file = new File(destFileName);
	    try {
	        FileOutputStream fout = new FileOutputStream(file);
	        int l = -1;
	        byte[] tmp = new byte[1024];
	        while ((l = in.read(tmp)) != -1) {
	            fout.write(tmp, 0, l);
	            // ע�����������OutputStream.write(buff)�Ļ���ͼƬ��ʧ�棬��ҿ�������
	        }
	        fout.flush();
	        fout.close();
	    } finally {
	        // �رյͲ�����
	        in.close();
	    }
	    httpclient.close();
	}
}