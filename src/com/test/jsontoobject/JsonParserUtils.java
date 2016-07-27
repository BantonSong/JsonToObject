package com.test.jsontoobject;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonParserUtils {

	/**
	 * Jsonת��Ϊ����
	 * @param cls��Ӧ��Object��class����
	 * @param jsonObject ��Ҫ������JSON��
	 * @return
	 */
	public static <T> T parseJsonToObject(Class<T> cls, JSONObject jsonObject) {
		T t = null;
		try {
			t = cls.newInstance();
			// �����ȡ���з���
			Method[] methods = cls.getDeclaredMethods();
			if (null != methods) {
				for (Method method : methods) {
					String methodName = method.getName();
					// ����set���������Խ��и�ֵ��set��������set+������������ĸ��д���Ĺ淶���Զ����ɼ���
					// ��struts�ѱ�����ʵ������ԭ��һ��
					if (methodName.startsWith("set")) {
						// ��ȡ��������
						String attributeName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

						// ��ȡset�����еĲ������ͣ�����ֻ��һ������
						Class<?> type = method.getParameterTypes()[0];
						// ���ݲ����������Ի�ȡJSON�ж�Ӧ�ֶε�ֵ�����и�ֵ����Ҫ�õ���invoke����
						try {
							// ��������
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
								// ��������
								List<?> list = null;
								if (list == null) {
									// ʵ����list����
									if (type.isInterface()) {
										list = new ArrayList();
									} else {
										list = (List<?>) type.newInstance();
									}

									// ��ȡJson����
									JSONArray jsonArray = jsonObject.optJSONArray(attributeName);
									// ��ȡList�ķ�������
									Class<?> listType = (Class<?>) ((ParameterizedType) cls.getDeclaredField(
											attributeName).getGenericType()).getActualTypeArguments()[0];
									// ΪList���Խ��и�ֵ
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
	 * ΪList���Խ��и�ֵ
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
		// �ݹ���ã���JsonתΪJava������ӵ�List��
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(parseJsonToObject(type, jsonArray.getJSONObject(i)));
		}
	}
}
