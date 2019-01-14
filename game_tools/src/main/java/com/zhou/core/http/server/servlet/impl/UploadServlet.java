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
	 * 默认使用
	 * @author zhouyongjun
	 *
	 以下列出的几点需要特别注意的
	　　1、为保证服务器安全，上传文件应该放在外界无法直接访问的目录下，比如放于WEB-INF目录下。
	　　2、为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名。
	　　3、为防止一个目录下面出现太多文件，要使用hash算法打散存储。
	　　4、要限制上传文件的最大值。
	　　5、要限制上传文件的类型，在收到上传文件名时，判断后缀名是否合法。
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
		   //3、判断提交上来的数据是否是上传表单的数据
		if (!ServletFileUpload.isMultipartContent(req))
		{
			//按照传统方式获取数据
			resp.getWriter().write("{\"code\":\"request content type is not multipart.\"}");
			return;
		}
		  //上传时生成的临时文件保存目录
        String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
		 File tmpFile = new File(tempPath);
         if (!tmpFile.exists()) {
             //创建临时目录
             tmpFile.mkdir();
         }
//		ServletFileUpload
		//http://www.cnblogs.com/xdp-gacl/p/4200090.html
		 //使用Apache文件上传组件处理文件上传步骤：
		 //1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
        factory.setSizeThreshold(1024*100);//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
        //设置上传时生成的临时文件的保存目录
        factory.setRepository(tmpFile);
        //2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置编码
		upload.setHeaderEncoding("UTF-8");
		//监控上传进度
		upload.setProgressListener(new ProgressListener() {
			
			@Override
			public void update(long pBytesRead, long pContentLength, int pItems) {
                System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead+",pItems:"+pItems);				
			}
		});
		  //设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是1MB
        upload.setFileSizeMax(1024*1024);
        //设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
        upload.setSizeMax(1024*1024*10);
        //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
		List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(req));
		String parentPath = "./webapps/upload/";
		for (FileItem fileItem : fileItems)
		{
			   //如果fileitem中封装的是普通输入项的数据
			if (fileItem.isFormField())
			{
				String name = fileItem.getFieldName();
				String value = fileItem.getString("UTF-8");
				System.out.println(name+" : " + value);
			}
			else
			{//如果fileitem中封装的是上传文件
                //得到上传的文件名称，
				String fileName = fileItem.getName();
				//注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
				//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
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
				//注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
				fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                //获取item中的上传文件的输入流
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
	      //创建一个文件输出流
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
    * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
    * @Anthor:孤傲苍狼
    * @param filename 文件的原始名称
    * @return uuid+"_"+文件的原始名称
    */ 
    private String makeFileName(String filename){  //2.jpg
        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUID.randomUUID().toString() + "_" + filename;
    }
    
    /**
     * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
    * @Method: makePath
    * @Description: 
    * @Anthor:孤傲苍狼
    *
    * @param filename 文件名，要根据文件名生成存储目录
    * @param savePath 文件存储路径
    * @return 新的存储目录
    */ 
    private String makePath(String filename,String savePath){
        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = filename.hashCode();
        int dir1 = hashcode&0xf;  //0--15
        int dir2 = (hashcode&0xf0)>>4;  //0-15
        //构造新的保存目录
        String dir = savePath + "\\" + dir1 + "\\" + dir2;  //upload\2\3  upload\3\5
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }
        return dir;
    }
}
