package com.zhou.dao.shrio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="shiro_user")
public class ShiroUserBean {
	@Id
	@GeneratedValue
	Integer id;
	@Column(unique=true,length=55)
	String name;
	@Column(length=128)
	String password;
	@OneToMany(cascade=CascadeType.ALL)
	List<ShiroRoleBean> roles;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public List<ShiroRoleBean> getRoles() {
		return roles;
	}
	public void setRoles(List<ShiroRoleBean> roles) {
		this.roles = roles;
	}
	
	
}
