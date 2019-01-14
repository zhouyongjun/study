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
	 * HttpClient连接SSL
	 */
	public void ssl() {
		CloseableHttpClient httpclient = null;
		try {
			
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));
			try {
				// 加载keyStore d:\\tomcat.keystore  
				trustStore.load(instream, "123456".toCharArray());
			} catch (CertificateException e) {
				e.printStackTrace();
			} finally {
				try {
					instream.close();
				} catch (Exception ignore) {
				}
			}
			// 相信自己的CA和所有自签名的证书
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
			// 只允许使用TLSv1协议
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			// 创建http请求(get方式)
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
	 * post方式提交表单（模拟用户登录请求）
	 */
	public void postForm() {
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceJ.action");
		// 创建参数队列  
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
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	public void post() {
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceJ.action");
		// 创建参数队列  
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
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送 get请求
	 */
	public void get() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.  
			HttpGet httpget = new HttpGet("http://www.baidu.com/");
			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.  
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体  
				HttpEntity entity = response.getEntity();
				System.out.println("--------------------------------------");
				// 打印响应状态  
				System.out.println(response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度  
					System.out.println("Response content length: " + entity.getContentLength());
					// 打印响应内容  
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
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 上传文件
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
	 * 上传文件 
	 * @throws ParseException
	 * @throws IOException
	 */   
	public static void postFile() throws ParseException, IOException{
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    try {
	        // 要上传的文件的路径
	        String filePath = new String("./zkclient-0.1.jar");
	        // 把一个普通参数和文件上传给下面这个地址 是一个servlet
	        HttpPost httpPost = new HttpPost("http://localhost:8081/upload");
	        // 把文件转换成流对象FileBody
	        File file = new File(filePath);
	        FileBody bin = new FileBody(file);  
	        StringBody uploadFileName = new StringBody("zkclient-0.1.jar", ContentType.create("multipart/form-data", Consts.UTF_8));
	        //以浏览器兼容模式运行，防止文件名乱码。  
	           HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
	                .addPart("uploadFile", bin) //uploadFile对应服务端类的同名属性<File类型>
//	                .addPart("uploadFileName", uploadFileName)//uploadFileName对应服务端类的同名属性<String类型>
	                .setCharset(CharsetUtils.get("UTF-8")).build();
	 
	        httpPost.setEntity(reqEntity);
	        System.out.println("发起请求的页面地址 " + httpPost.getRequestLine()+"\t contentType: " + reqEntity.getContentType().getValue());
	        // 发起请求 并返回请求的响应
	        CloseableHttpResponse response = httpClient.execute(httpPost);
	        try {
	            System.out.println("----------------------------------------");
	            // 打印响应状态
	            System.out.println(response.getStatusLine());
	            // 获取响应对象
	            HttpEntity resEntity = response.getEntity();
	            if (resEntity != null) {
	                // 打印响应长度
	                System.out.println("Response content length: "
	                        + resEntity.getContentLength());
	                // 打印响应内容
	                System.out.println(EntityUtils.toString(resEntity,
	                        Charset.forName("UTF-8")));
	            }
	            // 销毁
	            EntityUtils.consume(resEntity);
	        } finally {
	            response.close();
	        }
	    } finally {
	        httpClient.close();
	    }
	}
	 
	 /**
	 * 下载文件
	 * @param url            http://www.xxx.com/img/333.jpg
	 * @param destFileName   xxx.jpg/xxx.png/xxx.txt
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void getFile(String url, String destFileName)
	        throws ClientProtocolException, IOException {
	    // 生成一个httpclient对象
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
	            // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
	        }
	        fout.flush();
	        fout.close();
	    } finally {
	        // 关闭低层流。
	        in.close();
	    }
	    httpclient.close();
	}
}