package com.xlu.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	public static Object fromJsonToObject(String json, Type type) {
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}

	/**
	 *  {@code JSON}
	 * 
	 * @return string {@code JSON} json字符串
	 */
	public static String toJsonString(Object obj, Type type) {
		Gson gson = new Gson();
		return gson.toJson(obj, type);
	}

	public static String getJson(Object object) {
		return JSON.toJSONString(object);
	}

	
	public static <T> T getObject(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
		}
		return t;
	}

	public static <T> List<T> getList(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonString, cls);
		} catch (Exception e) {
			Log.v("123",e.getLocalizedMessage());
		}
		return list;
	}

	public static List<Map<String, Object>> listKeyMaps(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = JSON.parseObject(jsonString,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}
