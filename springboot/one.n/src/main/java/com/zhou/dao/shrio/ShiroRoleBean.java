package com.zhou.dao.shrio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="shiro_role")
public class ShiroRoleBean {
	@Id
	@GeneratedValue
	Integer id;
	@Column(unique=true,length=55)
	String name;
	@ManyToOne(fetch=FetchType.EAGER)
	ShiroUserBean user;
	@OneToMany(cascade=CascadeType.ALL)//1对多关系
	List<ShiroPermissionBean> permissions;
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
	public ShiroUserBean getUser() {
		return user;
	}
	public void setUser(ShiroUserBean user) {
		this.user = user;
	}
	public List<ShiroPermissionBean> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<ShiroPermissionBean> permissions) {
		this.permissions = permissions;
	}
	
}
