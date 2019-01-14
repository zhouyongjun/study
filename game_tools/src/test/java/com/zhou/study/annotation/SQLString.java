package com.zhou.study.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLString {
	//��ע����ʹ�õ�������Ϊvalueʱ�����丳ֵʱ���Բ�ָ�����Ե����ƶ�ֱ��д������ֵ�ӿڣ�����value����ı���������Ҫʹ��name=value�ķ�ʽ��ֵ��
	int value() default 0;
	String name() default "";
	Constraints constraints() default @Constraints;
}
