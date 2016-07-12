package com.potoman.webteam.data;

public enum KeyPreference {
	STRING_PSEUDO(String.class, "Pseudo"),
	STRING_PASSWORD(String.class, "Password");
	
	private Class class_ = null;
	private String keyName = null;
	
	KeyPreference(Class class_, String keyName) {
		this.class_ = class_;
		this.keyName = keyName;
	}
	
	public Class getType() {
		return class_;
	}
	
	public String getKeyName() {
		return keyName;
	}
}
