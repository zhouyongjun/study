<!DOCTYPE html>
<html>
	<head>
		<title>shiro</title>
	</head>
	<body>
		<h1>用户列表</h1>
		<table>
			<th>ID</th>
			<th>用户名</th>
			<th>拥有角色</th>
			<#if users??>
				<#list users as user>
					<tr>
						<td>${user.id}</td>
						<td>${user.name}</td>
						<td>
						<#if user.roles?? || user.roles?size == 0>
							<#list user.roles as role>
							${role.name},
							</#list>
						<#else>
							无角色
						</#if>
						</td>
					</tr>
				</#list>
			<#else>
			 暂无数据
			</#if>
			<tr>
			</tr>
		</table>
	</body>
</html?