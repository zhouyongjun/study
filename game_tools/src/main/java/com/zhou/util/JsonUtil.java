package com.zhou.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	public static  <T> T fromJson(String json,Class<T> classOfT)
	{
		if (json == null || json.length() == 0) return null;
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, new TypeToken<T>(){}.getType());
	}
	
	public static  <T> T fromJson(String json,TypeToken<T> token)
	{
		if (json == null || json.length() == 0) return null;
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, token.getType());
	}
	
	public static <T> String toJson(T t)
	{
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
		return gson.toJson(t);
	}
}
