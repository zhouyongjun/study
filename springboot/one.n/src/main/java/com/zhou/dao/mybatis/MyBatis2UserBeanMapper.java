package com.zhou.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhou.dao.jpa.entity.UserBean;

@Mapper
public interface MyBatis2UserBeanMapper {
	List<UserBean> getUsers();
	
	UserBean getUserById(long id);
	
	  /**
     * 修改用户信息
     * 
     * @param user
     */
	void update(UserBean user);
	
	  /**
     * 删除用户
     * 
     * @param id用户id
     */
	void del(@Param("id")long id);
	
	/**
     * 新增一条用户信息
     * 
     * @param user
     */
	 void save(UserBean user);
}
