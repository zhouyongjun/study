package com.zhou.core.http.server.servlet.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.zhou.core.http.server.servlet.AbstractServlet;
	/**
	 * Ĭ��ʹ��
	 * @author zhouyongjun
	 *
	 �����г��ļ�����Ҫ�ر�ע���
	����1��Ϊ��֤��������ȫ���ϴ��ļ�Ӧ�÷�������޷�ֱ�ӷ��ʵ�Ŀ¼�£��������WEB-INFĿ¼�¡�
	����2��Ϊ��ֹ�ļ����ǵ���������ҪΪ�ϴ��ļ�����һ��Ψһ���ļ�����
	����3��Ϊ��ֹһ��Ŀ¼�������̫���ļ���Ҫʹ��hash�㷨��ɢ�洢��
	����4��Ҫ�����ϴ��ļ������ֵ��
	����5��Ҫ�����ϴ��ļ������ͣ����յ��ϴ��ļ���ʱ���жϺ�׺���Ƿ�Ϸ���
	 */
public class UploadServlet extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getHttpServletName() {
		return "upload";
	}

	@Override
	public String getPattern() {
		return "/upload";
	}

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		System.out.println(this.getClass().getSimpleName()+"  method:"+req.getMethod()+",ContentType:"+req.getContentType());
		   //3���ж��ύ�����������Ƿ����ϴ���������
		if (!ServletFileUpload.isMultipartContent(req))
		{
			//���մ�ͳ��ʽ��ȡ����
			resp.getWriter().write("{\"code\":\"request content type is not multipart.\"}");
			return;
		}
		  //�ϴ�ʱ���ɵ���ʱ�ļ�����Ŀ¼
        String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
		 File tmpFile = new File(tempPath);
         if (!tmpFile.exists()) {
             //������ʱĿ¼
             tmpFile.mkdir();
         }
//		ServletFileUpload
		//http://www.cnblogs.com/xdp-gacl/p/4200090.html
		 //ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
		 //1������һ��DiskFileItemFactory����
		DiskFileItemFactory factory = new DiskFileItemFactory();
        //���ù����Ļ������Ĵ�С�����ϴ����ļ���С�����������Ĵ�Сʱ���ͻ�����һ����ʱ�ļ���ŵ�ָ������ʱĿ¼���С�
        factory.setSizeThreshold(1024*100);//���û������Ĵ�СΪ100KB�������ָ������ô�������Ĵ�СĬ����10KB
        //�����ϴ�ʱ���ɵ���ʱ�ļ��ı���Ŀ¼
        factory.setRepository(tmpFile);
        //2������һ���ļ��ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);
		//���ñ���
		upload.setHeaderEncoding("UTF-8");
		//����ϴ�����
		upload.setProgressListener(new ProgressListener() {
			
			@Override
			public void update(long pBytesRead, long pContentLength, int pItems) {
                System.out.println("�ļ���СΪ��" + pContentLength + ",��ǰ�Ѵ���" + pBytesRead+",pItems:"+pItems);				
			}
		});
		  //�����ϴ������ļ��Ĵ�С�����ֵ��Ŀǰ������Ϊ1024*1024�ֽڣ�Ҳ����1MB
        upload.setFileSizeMax(1024*1024);
        //�����ϴ��ļ����������ֵ�����ֵ=ͬʱ�ϴ��Ķ���ļ��Ĵ�С�����ֵ�ĺͣ�Ŀǰ����Ϊ10MB
        upload.setSizeMax(1024*1024*10);
        //4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
		List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(req));
		String parentPath = "./webapps/upload/";
		for (FileItem fileItem : fileItems)
		{
			   //���fileitem�з�װ������ͨ�����������
			if (fileItem.isFormField())
			{
				String name = fileItem.getFieldName();
				String value = fileItem.getString("UTF-8");
				System.out.println(name+" : " + value);
			}
			else
			{//���fileitem�з�װ�����ϴ��ļ�
                //�õ��ϴ����ļ����ƣ�
				String fileName = fileItem.getName();
				//ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺  c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
				//�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
				fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
				String fieldName =fileItem.getFieldName();
				String path = parentPath;
				if (fieldName != null && fieldName.startsWith("/") && fieldName.length() > 1)
				{
					path += fieldName.substring(1);
					if (!path.endsWith("/")) path  += "/";
					File pathFile = new File(path);
					if (!pathFile.exists())
					{
						pathFile.mkdirs();
					}
				}
				
				System.out.println("fileName : " + fileName);
				if (fileName == null || fileName.isEmpty())
				{
					continue;
				}
				//ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺  c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
                //�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
				fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                //��ȡitem�е��ϴ��ļ���������
//				write(fileItem.getInputStream(),path +  fileName);
				fileItem.write(new File(path +  fileName));
                fileItem.delete();
			}
		}
		resp.getWriter().write("{\"code\":\"file upload success.\"}");
	}


	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		post(req, resp);
	}

	
	private void write(InputStream in, String pathFileName) throws Exception {
	      //����һ���ļ������
	      FileOutputStream out = new FileOutputStream(pathFileName);
	      byte[] bytes = new byte[1024];
	      int length = 0;
	      while((length = in.read(bytes)) > 0)
	      {
	      	out.write(bytes, 0, length);
	      	out.flush();
	      	bytes = new byte[1024]; 
	      	length = 0;
	      }
	      in.close();
	      out.close();		
		}
	
  /**
    * @Method: makeFileName
    * @Description: �����ϴ��ļ����ļ������ļ����ԣ�uuid+"_"+�ļ���ԭʼ����
    * @Anthor:�°�����
    * @param filename �ļ���ԭʼ����
    * @return uuid+"_"+�ļ���ԭʼ����
    */ 
    private String makeFileName(String filename){  //2.jpg
        //Ϊ��ֹ�ļ����ǵ���������ҪΪ�ϴ��ļ�����һ��Ψһ���ļ���
        return UUID.randomUUID().toString() + "_" + filename;
    }
    
    /**
     * Ϊ��ֹһ��Ŀ¼�������̫���ļ���Ҫʹ��hash�㷨��ɢ�洢
    * @Method: makePath
    * @Description: 
    * @Anthor:�°�����
    *
    * @param filename �ļ�����Ҫ�����ļ������ɴ洢Ŀ¼
    * @param savePath �ļ��洢·��
    * @return �µĴ洢Ŀ¼
    */ 
    private String makePath(String filename,String savePath){
        //�õ��ļ�����hashCode��ֵ���õ��ľ���filename����ַ����������ڴ��еĵ�ַ
        int hashcode = filename.hashCode();
        int dir1 = hashcode&0xf;  //0--15
        int dir2 = (hashcode&0xf0)>>4;  //0-15
        //�����µı���Ŀ¼
        String dir = savePath + "\\" + dir1 + "\\" + dir2;  //upload\2\3  upload\3\5
        //File�ȿ��Դ����ļ�Ҳ���Դ���Ŀ¼
        File file = new File(dir);
        //���Ŀ¼������
        if(!file.exists()){
            //����Ŀ¼
            file.mkdirs();
        }
        return dir;
    }
}
