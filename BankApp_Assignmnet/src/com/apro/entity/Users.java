package com.apro.entity;

public class Users {
	private int user_id;
	private String username;
	private String user_type;
	public Users(int user_id, String username, String user_type) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.user_type = user_type;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	
}
