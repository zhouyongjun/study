package com.zhou.core.esb.common;

public final class Const {
	public static void log(Object... logs) {
		StringBuffer sb = new StringBuffer();
		for (Object l : logs) 
		{
			sb.append(l).append("\t");
		}
		System.out.println(sb.toString());
	}
}
