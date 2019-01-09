package com.zhou.servlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;
/**
 * 配置监控统计功能
 * @author zhouyongjun
 * 配置Servlet
	如下是在SpringBoot项目中基于注解的配置，如果是web.xml配置，按规则配置即可。
	以上配置的监控方式是使用了原生的servlet，filter方式，
	然后通过@ServletComponentScan进行启动扫描包的方式进行处理的，你会发现我们的servlet，filter根本没有任何的编码。
 */
@WebServlet(
		urlPatterns={"/druid/*"},
		initParams={
				@WebInitParam(name="allow",value="127.0.0.1"),
				@WebInitParam(name="loginUsername",value="root"),
				@WebInitParam(name="loginPassword",value="123"),
				@WebInitParam(name="resetEnable",value="true")//允许HTML页面上的"Reset All" 功能
		}
			)
public class DruidStatViewServlet extends StatViewServlet implements Servlet{
	private static final long serialVersionUID = 1L;

}
