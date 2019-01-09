package com.zhou.dao.shrio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="shiro_permission")
public class ShiroPermissionBean {
	@Id
	@GeneratedValue
	Integer id;
	@Column(length=55)
	String name;
	@Column(unique=true)
	String permission;
	@ManyToOne(fetch=FetchType.EAGER)
	ShiroRoleBean role;
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
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public ShiroRoleBean getRole() {
		return role;
	}
	public void setRole(ShiroRoleBean role) {
		this.role = role;
	}
	
	
}

