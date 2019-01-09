package com.zhou.dao.jpa.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zhou.dao.jpa.entity.UserBean;
/**
 * 集成底层jpa存储类
 * 提供公共的数据库CRUD方法
 * @author zhouyongjun
 *
 *	实际上JpaRepository实现了PagingAndSortingRepository接口，
 *		PagingAndSortingRepository接口实现了CrudRepository接口，CrudRepository接口实现了Repository接口；
	简单说明下：
		Repository接口是一个标识接口，里面是空的；
		CrudRepository接口定义了增删改查方法；
		PagingAndSortingRepository接口用于分页和排序；
		由于JpaRepository接口继承了以上所有接口，所以拥有它们声明的所有方法；
		另外注意下，以findAll方法为例，JpaRepository接口返回的是List, PagingAndSortingRepository和CrudRepository返回的是迭代器
 */
public interface UserBeanMapper extends JpaRepository<UserBean, Long>{
	@Query(nativeQuery=true,value="select * from user where name=?1 and password=?2")
	public UserBean findByNameAndPassword(String name,String password);
//	 @Query(value = "select * from #{#entityName} u where u.name=?1", nativeQuery = true)
//	List<UserBean> findByName2(String name);
	//由于在实体类中声明了@NamedQuery注解，实际上findByName方法会使用@NamedQuery注解标注的查询语句去查询；
    List<UserBean> findByName(String name);
}
