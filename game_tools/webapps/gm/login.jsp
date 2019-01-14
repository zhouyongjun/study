<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>游戏后台</title>
        <meta charset="UTF-8">
        <meta name="viewport"  content="width=device-width,initial-scale=1.0">
        <link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
    </head>
    <body>
    <div class="container">
	    <form class="form-horizontal center-block" action="/gm/login" method="post">
	    	<div class="form-group">
	    		<label for="user" class="col-sm-2 control-label">用户</label>
	    		<div class="col-sm-4">
	    		<input type="text" id="user" class="form-control" name="user" placeholder="输入用户名..."/>
	    		</div>
	    	</div>
	    	<div class="form-group">
	    		<label class="col-sm-2 control-label">密码</label>
	    		<div class="col-sm-4">
	    			<input type="password" class="form-control" name="password" placeholder="输入密码..."/>
	    		</div>
	    	</div>
	    	<div class="form-group">
	    		<button type="submit" class="btn btn-primary col-xs-offset-3">登录</button>
	    	</div>
	    </form>
    	</div>
    </body>
</html>