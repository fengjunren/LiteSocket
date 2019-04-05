package com.litesocket.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {

	private static Gson gson = new Gson();

	public static <T> T deserialize(String json, TypeToken<T> types) {
		return (T) gson.fromJson(json, types.getType());
	}

	public static <T> T deserialize(String json, Class<T> clazz) {
		return (T) gson.fromJson(json, clazz);
	}

	public static String serialize(Object object) {
		return gson.toJson(object);
	}

}
