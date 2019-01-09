<!DOCTYPE html>
<html>
	<head>
		<title>文件处理</title>
	</head>
	<body>
		<form method="post" enctype="multipart/form-data" action="/files/upload">
			文件: <input type="file" name="file" value="选择文件..."/>
			<input type="submit" value="上传" />
		</form>
	</body>
</html>