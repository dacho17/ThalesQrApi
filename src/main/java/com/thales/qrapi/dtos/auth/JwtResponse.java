package com.thales.qrapi.dtos.auth;

public class JwtResponse {
	
	private String token;
	private String type;
	private int userId;
	private String username;
	private String role;

	public JwtResponse() {}
	
	public JwtResponse(String token, int userId, String username, String role) {
		super();
		this.token = token;
		this.type = "Bearer";
		this.userId = userId;
		this.username = username;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + ", userId=" + userId + ", username=" + username
				+ ", role=" + role + "]";
	}
}
