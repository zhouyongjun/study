package com.zhou.study.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TableCreatorMain {
 public static void main(String[] args) {
	try {
		Class<?> clazz = Class.forName("com.zhou.study.annotation.Member");
		DBTable dbtTable = clazz.getAnnotation(DBTable.class);
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ")
		.append(dbtTable.name())
		.append(" (\n");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) 
		{
			Annotation[] annotations = field.getAnnotations();
			if (annotations == null || annotations.length == 0) continue;
			Annotation ann = annotations[0];
			if (ann instanceof SQLString)
			{
				SQLString sqlString = (SQLString) ann;				
				String columnName = sqlString.name();
				if (columnName.length() == 0)columnName = field.getName();
				sb.append(columnName).append(" varchar(").append(sqlString.value()).append(") ")
				.append(getConstraints(sqlString.constraints())).append(",\n");
			}
			else if (ann instanceof SQLInteger)
			{
				SQLInteger sqlInteger = (SQLInteger) ann;				
				String columnName = sqlInteger.name();
				if (columnName.length() == 0)columnName = field.getName();
				sb.append(columnName).append(" int(10) ")
				.append(getConstraints(sqlInteger.constraints())).append(",\n");
			}
		}
		sb.append(");");
		System.out.println(sb.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
}

 private static String getConstraints(Constraints constraints) {
	 StringBuffer sb = new StringBuffer();
	 if (constraints == null) return "";
	 if (!constraints.allowNull())sb.append(" NOT NULL");
	 if (constraints.primaryKey()) sb.append(" PRIMARY KEY");
	 if (constraints.unique()) sb.append(" UNIQUE");
	return sb.toString();
}
}
