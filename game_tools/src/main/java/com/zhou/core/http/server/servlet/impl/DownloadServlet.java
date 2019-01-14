package com.zhou.core.http.server.servlet.impl;

import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.zhou.core.http.server.servlet.AbstractServlet;

public class DownloadServlet extends AbstractServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String fileName = req.getParameter("filename");
		int lastIndexOfPoint = fileName.lastIndexOf(".");
        String suffix = fileName.substring(lastIndexOfPoint);
		String parentPath = "./webapps/download/";
		FileInputStream in = new FileInputStream(parentPath+fileName);
		OutputStream out = resp.getOutputStream();
		/*
		application/octet-stream
		1��ֻ���ύ�����ƣ�����ֻ���ύһ�������ƣ�����ύ�ļ��Ļ���ֻ���ύһ���ļ�,��̨���ղ���ֻ����һ��������ֻ�������������ֽ����飩
		������������ͻ��˷����ر����ӣ����·���˰��쳣��
		12:26:12.837 [http-bio-8081-exec-1] ERROR com.zhou.core.http.server.servlet.AbstractServlet - http :127.0.0.1  session[FBCD413FA5A98246504FC0C8CB0FF1AD] doPost is error.
	org.apache.catalina.connector.ClientAbortException: java.net.SocketException: Connection reset by peer: socket write error
	at org.apache.catalina.connector.OutputBuffer.realWriteBytes(OutputBuffer.java:410) ~[tomcat-embed-core-7.0.73.jar:7.0.73]
	at org.apache.tomcat.util.buf.ByteChunk.flushBuffer(ByteChunk.java:480) ~[tomcat-embed-core-7.0.73.jar:7.0.73]
	at org.apache.tomcat.util.buf.ByteChunk.append(ByteChunk.java:366) ~[tomcat-embed-core-7.0.73.jar:7.0.73]
	at org.apache.catalina.connector.OutputBuffer.writeBytes(OutputBuffer.java:435) ~[tomcat-emb
		 */
		resp.setContentType("application/x-a11");
//		resp.addHeader("Content-Disposition","attachment;filename="+fileName);
		resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		 
		int size = IOUtils.copy(in, out);
//		byte[] reads = new byte[10240];
//		int length = 0;
//		int size = 0;
//		while(((length = in.read(reads))) > 0)
//		{
//			out.write(reads, 0, length);
//			reads = new byte[10240];
//			size += length;
//			length = 0;
//		}
		resp.addHeader("Content-Length",""+size);
		out.flush();
		in.close();
		out.close();
	}

	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		post(req, resp);
	}

	@Override
	public String getHttpServletName() {
		return "download";
	}

	@Override
	public String getPattern() {
		return "/download";
	}

}
