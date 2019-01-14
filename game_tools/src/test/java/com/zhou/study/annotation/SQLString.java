package com.zhou.study.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLString {
	//当注解中使用的属性名为value时，对其赋值时可以不指定属性的名称而直接写上属性值接口；除了value意外的变量名都需要使用name=value的方式赋值。
	int value() default 0;
	String name() default "";
	Constraints constraints() default @Constraints;
}
