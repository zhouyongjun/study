<%@page import="com.zhou.core.gm.bs.servlet.control.ServerParam"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <head>
		<title>开服后台</title>
		<!-- <link rel="stylesheet" type="text/css" href="css/t.css" />
		<script type="text/javascript" defer="defer" src="js/t.js"></script>  -->
	</head>
	<body>
		<div>
		<form action="/gm/control/newserver" method="post">
			<div>
				<table>
					<tbody>
						<%
							ServerParam[] serverParams = ServerParam.getTableValues(0);
						for (int i=0;i<serverParams.length;i++)
						{
							ServerParam param = serverParams[i];	
						%>
							<tr><td><%= param.getShowName() %></td><td><input name="<%= param.getParamName()%>" value="<%= param.getDefaultValue()%>" /></td></tr>
						<%}%>
						<tr>
							<td>服务器组:</td>
							<td>
								<select name="select-group">
									<%
									String[] options = new String[]{"组1","组2","组3","组4"};
										for(int i=0;i<options.length;i++) {
										%>
											<option value='sg_<%= i %>'><%= options[i]
										%>
									<%}%>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
				<table border="1" onabort="1" id="table-1">
					<tr>
						<td>新服参数</td> <td>VALUES</td> 
						</tr>
					<tbody>
						<%
							serverParams = ServerParam.getTableValues(1);
						for (int i=0;i<serverParams.length;i++)
						{
							ServerParam param = serverParams[i];	
						%>
							<tr><td><%= param.getShowName() %></td><td><input name="<%= param.getParamName()%>" value="<%= param.getDefaultValue()%>" /></td></tr>
						<%}%>
					</tbody>
				</table>
			</div>
			<div>
				<input type="submit" value="提交"/>
			</div>
			</form>
		</div>
	</body>
</html>


