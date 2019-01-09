<!DOCTYPE html>
<html>
	<head>
		<title>JPA 测试</title>
		<meta charset="UTF-8" >
	</head>
	<body>
		<table>
			<th>编号</th>
			<th>姓名</th>
			<th>密码</th>
			<#if users??>
			<#-- 
				<tr><td>数据存在</td></tr>-->
				<#list users as user>
					<tr>
						<td>${user.id}</td>
						<td>${user.name}</td>
						<td>${user.password}</td>
					</tr>	
				</#list>
			<#else>
				<tr><td>数据不存在</td></tr>	
			</#if>
		</table>
	</body>
</html>