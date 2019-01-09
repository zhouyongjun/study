package com.zhou.domain.entity;
import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long country;
	private long id;
	String name;
	String desc;
	public User(long country, long id, String name, String desc) {
		super();
		this.country = country;
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
	public long getCountry() {
		return country;
	}
	public void setCountry(long country) {
		this.country = country;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(country).append(",")
		.append(id).append(",").append(name).append(",")
		.append(desc).append("]").append(super.toString());
		return sb.toString();
	}
	
}
