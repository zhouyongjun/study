package com.zhou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.zhou.oldservlet.MyFilter;
import com.zhou.oldservlet.MyListener;
import com.zhou.oldservlet.MyServlet;

/*@Configuration
@EnableAutoConfiguration
@ComponentScan*/
@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class ApplicationDev {
	
	@Bean//注册Servlet 路径名和对象
	public ServletRegistrationBean servletRegistrationBean()
	{
		return new ServletRegistrationBean(new MyServlet(), "/servlet");
	}
	@Bean//注册指定servlet路径的过滤器
	public FilterRegistrationBean filterRegistrationBean()
	{
		return new FilterRegistrationBean(new MyFilter(), servletRegistrationBean());
	}
	@Bean//注册Servlet应用启动、销毁时的监听
	public ServletListenerRegistrationBean<MyListener> servletListenerRegistrationBean()
	{
		return new ServletListenerRegistrationBean<MyListener>(new MyListener());
	}
	
      public static void main(String[] args) {
    	  ApplicationContext applicationContext = SpringApplication.run(ApplicationDev.class, args);
          for (String name : applicationContext.getBeanDefinitionNames()) {
//              System.out.println(name);
          }
      }
      
      
}
