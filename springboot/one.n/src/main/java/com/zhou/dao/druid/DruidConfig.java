package com.zhou.dao.druid;


import javax.sql.DataSource;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration	//标识该类被纳入spring容器中实例化并管理
//@ServletComponentScan	//用于扫描所有的Servlet、filter、listener
public class DruidConfig {
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.druid")//加载时读取指定的配置信息,前缀为spring.datasource.druid
	public DataSource druidDataSource()
	{
		return new DruidDataSource();
	}
	
	 public ServletRegistrationBean DruidStatViewServle2(){
	       //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
	       ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid2/*");
	      
	       //添加初始化参数：initParams
	      
	       //白名单：
	       servletRegistrationBean.addInitParameter("allow","127.0.0.1");
	       //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
	       servletRegistrationBean.addInitParameter("deny","192.168.1.73");
	       //登录查看信息的账号密码.
	       servletRegistrationBean.addInitParameter("loginUsername","admin2");
	       servletRegistrationBean.addInitParameter("loginPassword","123456");
	       //是否能够重置数据.
	       servletRegistrationBean.addInitParameter("resetEnable","false");
	       return servletRegistrationBean;
	    }

	 /**
	     * 注册一个：filterRegistrationBean
	     * @return
	     */
    @Bean
    public FilterRegistrationBean druidStatFilter2(){
      
       FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
      
       //添加过滤规则.
       filterRegistrationBean.addUrlPatterns("/*");
      
       //添加不需要忽略的格式信息.
    filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
       return filterRegistrationBean;
    }
	   
}
