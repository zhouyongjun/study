package com.zhou.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhou.dao.shrio.ShiroPermissionBean;
import com.zhou.dao.shrio.ShiroRoleBean;
import com.zhou.dao.shrio.ShiroUserBean;
import com.zhou.service.IShiroService;
/**
 * 自定义 用户 认证和授权 
 * @author zhouyongjun
 *
 */
public class MyShiroRealM extends AuthorizingRealm {
	@Autowired
	IShiroService service;
	/**
	 * 授权:对用户权限和对应权限添加
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取登录用户名
		String name = (String)principals.getPrimaryPrincipal();
		//查询用户
		ShiroUserBean user = service.findByName(name);
		//添加角色和权限
		SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
		for (ShiroRoleBean role : user.getRoles())
		{
			//添加角色
			auth.addRole(role.getName());
			for (ShiroPermissionBean permission : role.getPermissions())
			{
				//添加权限
				auth.addStringPermission(permission.getPermission());
			}
		}
		return auth;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String name = upToken.getUsername();
		String password = new String(upToken.getPassword());
		ShiroUserBean user = service.findByName(name);
		SimpleAuthenticationInfo auth = new SimpleAuthenticationInfo(name, user.getPassword(), getName());
		return auth;
	}

}
