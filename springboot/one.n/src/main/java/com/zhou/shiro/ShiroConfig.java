package com.zhou.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
	/**将自己的验证方式 加入容器 @return*/
	@Bean
	public MyShiroRealM myShiroRealM()
	{
		MyShiroRealM realm = new MyShiroRealM();
		return realm;
	}
	/** 权限管理 配置 RealM的管理认证 @return*/
	@Bean
	public SecurityManager securityManager()
	{
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(myShiroRealM());
		return securityManager;
	}
	/**
	 * Filter工厂，设置对应的过滤条件和跳转条件
	 * @param securityManager
	 * @return
	 */
	@Bean
	ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager)
	{
		ShiroFilterFactoryBean bean = new  ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);//
		/*过滤器 定义 监测路径配置 */
		Map<String,String> filterChainDefinitionMap = new HashMap<>();
		filterChainDefinitionMap.put("/shiro/logout", DefaultFilter.logout.name());//退出
		filterChainDefinitionMap.put("/shiro/user/**", DefaultFilter.authc.name());//认证
		filterChainDefinitionMap.put("/shiro/guest/**", DefaultFilter.anon.name());//认证
		bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		bean.setLoginUrl("/shiro/loginUrl");//登录
		bean.setSuccessUrl("/shiro/index");//成功
		bean.setUnauthorizedUrl("/shiro/errorShiro");//未授权
		return bean;
	}
}
