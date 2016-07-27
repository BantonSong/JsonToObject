package com.test.jsontoobject;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonParserUtils {

	/**
	 * Json转化为对象
	 * @param cls对应的Object的class对象
	 * @param jsonObject 需要解析的JSON串
	 * @return
	 */
	public static <T> T parseJsonToObject(Class<T> cls, JSONObject jsonObject) {
		T t = null;
		try {
			t = cls.newInstance();
			// 反射获取所有方法
			Method[] methods = cls.getDeclaredMethods();
			if (null != methods) {
				for (Method method : methods) {
					String methodName = method.getName();
					// 根据set方法对属性进行赋值，set方法符合set+属性名（首字母大写）的规范，自动生成即可
					// 和struts把表单数据实例化的原理一致
					if (methodName.startsWith("set")) {
						// 获取属性名称
						String attributeName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

						// 获取set方法中的参数类型，有且只有一个参数
						Class<?> type = method.getParameterTypes()[0];
						// 根据参数类型来对获取JSON中对应字段的值并进行赋值，主要用到了invoke方法
						try {
							// 基本类型
							if (type == boolean.class || type == Boolean.class) {
								method.invoke(t, new Object[] { jsonObject.getBoolean(attributeName) });
							} else if (type == byte.class || type == Byte.class) {
								method.invoke(t, new Object[] { jsonObject.getInt(attributeName) });
							} else if (type == char.class || type == Character.class) {
								method.invoke(t, new Object[] { jsonObject.getInt(attributeName) });
							} else if (type == short.class || type == Short.class) {
								method.invoke(t, new Object[] { jsonObject.getInt(attributeName) });
							} else if (type == int.class || type == Integer.class) {
								method.invoke(t, new Object[] { jsonObject.getInt(attributeName) });
							} else if (type == long.class || type == Long.class) {
								method.invoke(t, new Object[] { jsonObject.getLong(attributeName) });
							} else if (type == float.class || type == Float.class) {
								method.invoke(t, new Object[] { jsonObject.getDouble(attributeName) });
							} else if (type == double.class || type == Double.class) {
								method.invoke(t, new Object[] { jsonObject.getDouble(attributeName) });
							} else if (type == String.class) {
								method.invoke(t, new Object[] { jsonObject.getString(attributeName) });
							} else if (List.class.isAssignableFrom(type)) {
								// 集合类型
								List<?> list = null;
								if (list == null) {
									// 实例化list对象
									if (type.isInterface()) {
										list = new ArrayList();
									} else {
										list = (List<?>) type.newInstance();
									}

									// 获取Json数组
									JSONArray jsonArray = jsonObject.optJSONArray(attributeName);
									// 获取List的泛型类型
									Class<?> listType = (Class<?>) ((ParameterizedType) cls.getDeclaredField(
											attributeName).getGenericType()).getActualTypeArguments()[0];
									// 为List属性进行赋值
									decodeList(list, jsonArray, listType);
									method.invoke(t, new Object[] { list });
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 为List属性进行赋值
	 * 
	 * @param list
	 * @param jsonArray
	 * @param type
	 * @throws Exception
	 */
	private static void decodeList(List list, JSONArray jsonArray, Class type) throws Exception {
		if (jsonArray == null) {
			return;
		}
		// 递归调用，把Json转为Java对象并添加到List中
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(parseJsonToObject(type, jsonArray.getJSONObject(i)));
		}
	}
}
