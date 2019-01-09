package com.zhou.dao.jpa.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhou.dao.shrio.ShiroUserBean;

public interface ShiroUserBeanMapper extends JpaRepository<ShiroUserBean, Integer>{

	public ShiroUserBean findByName(String name);
}
