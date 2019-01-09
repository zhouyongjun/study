package com.zhou.controller.file;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
public class FileController {
	
	@RequestMapping("/index")
	public String index()
	{
		return "file/index";
	}
	@RequestMapping("/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file)
	{
		if (file == null || file.isEmpty()) return "文件不存在";
		String originalFileName = file.getOriginalFilename();
		System.err.println("文件名："+originalFileName);
				// 获取文件的后缀名
		String suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));
		System.err.println("后缀名："+suffixName);
//		String targetFileName = "./"+UUID.randomUUID().toString()+suffixName;//解决linux中文乱码
		//目标文件需要绝对路径才能传送成功
		String targetFileName = "D:\\new_online_project\\springboot\\filetemp\\"+originalFileName;
		File destFile = new File(targetFileName);
		try {
			file.transferTo(destFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "上传失败";
		}
		System.out.println(destFile.getAbsolutePath());
		return "上传成功";
	}
}
