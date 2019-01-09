package com.zhou.dao.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity//表示实体
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })  
@Table(name="user")//表示表名字
/*
 * 注意下这里的@NamedQuery注解，大致意思就是让我们在Repository接口中定义的findByName方法不使用默认的查询实现，
 * 	取而代之的是使用这条自定义的查询语句去查询，如果这里没有标注的话，会使用默认实现的
 */
@NamedQuery(name = "UserBean.findByName", query = "select id,name,password from UserBean u where u.name=?1")
public class UserBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id//表示主键
	@GeneratedValue//表示自增长
	private Long id;
	@Column//字段
	private String name;
	@Column
	private String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "UserBean [id=" + id + ", userName=" + name + ", passWord=" + password + "]";
	}
}
