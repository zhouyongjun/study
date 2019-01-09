package com.zhou.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.zhou.dao.jpa.entity.UserBean;

@Mapper//Mapper注解
public interface MyBatisUserBeanMapper {
	@Select("select * from user")//select差选
	@Results({//结果字段、属性关联
		@Result(column="id",property="id",jdbcType=JdbcType.BIGINT),
		@Result(column="name",property="name",jdbcType=JdbcType.VARCHAR),
		@Result(column="password",property="password",jdbcType=JdbcType.VARCHAR),
		})
	List<UserBean> getUsers();
	
	@Select("select * from user where id=#{id}")
	@Results({
		@Result(column="id",property="id",jdbcType=JdbcType.BIGINT),
		@Result(column="name",property="name",jdbcType=JdbcType.VARCHAR),
		@Result(column="password",property="password",jdbcType=JdbcType.VARCHAR),
		})
	UserBean getUserById(long id);
	
	@Update("update user set name=#{name},password=#{password} where id=#{id}")//更新
	void update(UserBean user);
	
	@Delete("delete from user where id =#{id}")//删除
	void del(long id);
	
	@Insert("insert into user(name,password) value(#{name},#{password})")//插入
	 void save(UserBean user);
}
