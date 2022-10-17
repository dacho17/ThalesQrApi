package com.thales.qrapi.dtos.auth;

import com.thales.qrapi.dtos.enums.UserRole;

public class SignupRequest {
	
	private String username;
	private String password;
	private UserRole role;
	
	public SignupRequest() {}

	public SignupRequest(String username, String password, UserRole role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "SignupRequest [username=" + username + ", password=" + password + ", role=" + role + "]";
	}
}
