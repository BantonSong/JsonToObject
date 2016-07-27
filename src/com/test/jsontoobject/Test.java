package com.test.jsontoobject;

import java.lang.reflect.Field;

import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject("{id:123;name:'ttttt'}");

		Class class1 = Product.class;
		Field[] f = class1.getDeclaredFields();
		for (int i = 0; i < f.length; i++) {
			System.out.println(f[i].getName());
		}

		Product product = JsonParserUtils.parseJsonToObject(class1, jsonObject);
		if (product != null) {
			System.out.println(product.toString());
		}

		JSONObject jsonObject1 = new JSONObject(
				"{age:10;name:'Banton';gender:'man';persons:[{age:11;name:'Banton1';gender:'woman'}]}");

		Class class2 = Person.class;

		Person person = JsonParserUtils.parseJsonToObject(class2, jsonObject1);
		if (person != null) {
			System.out.println(person.toString());
		}
	}
}
