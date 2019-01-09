package com.zhou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @ConfigurationProperties 配置 获取及其设置
 * @author zhouyongjun
 *
 */
@Component
@ConfigurationProperties(prefix="spring.datasource.druid")
public class DruidMySqlConfiguration {
	private String url;
	private String username;
	private String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
