package com.thales.qrapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.thales.qrapi.dtos.enums.UserRole;
import com.thales.qrapi.entities.User;

public class UserDetailsHelper implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String username;
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsHelper(int id, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}
	
	public static UserDetailsHelper build(User user) {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(UserRole.getRole(user.getRoleType()).name()));
		
		return new UserDetailsHelper(
			user.getId(),
			user.getUsername(),
			user.getPassword(),
			authorities);
	}

	public int getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
