package com.test.jsontoobject;

import java.util.List;

public class Person {
	private int age;
	private String name;
	private String gender;
	private List<Person> persons;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {
		String str = "\nname:" + name + "\nage:" + age + "\ngender:" + gender;
		if (persons != null) {
			for (Person person : persons) {
				str += "\n" + person.toString();
			}
		}
		return str;
	}
}
