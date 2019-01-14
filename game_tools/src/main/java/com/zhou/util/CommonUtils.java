package com.zhou.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class CommonUtils {

	
	public static <T, P> List<T> getList(List<P> parents,Class<? extends T> fclass)
	{
		if (parents == null || parents.isEmpty()) return null;
		List<T> list = new ArrayList<T>();
		for (P p : parents) 
		{
			try {
				list.add(fclass.cast(p));
			} catch (Exception e) {
			}
		}
		return list;
		
	}
	
}
