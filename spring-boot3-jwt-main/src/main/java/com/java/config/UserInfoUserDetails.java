package com.java.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.java.entity.UserInfo;

public class UserInfoUserDetails implements UserDetails {

	// Fields to hold user information
	private String name;
	private String password;
	private List<GrantedAuthority> authorities;

	// Constructor to create UserDetails object from UserInfo entity
	public UserInfoUserDetails(UserInfo userInfo) {
		// Extract user information from UserInfo entity
		name = userInfo.getName();
		password = userInfo.getPassword();
		// Convert roles string to list of GrantedAuthority objects
		authorities = Arrays.stream(userInfo.getRoles().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	// Get authorities granted to the user
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	// Get user password
	@Override
	public String getPassword() {
		return password;
	}

	// Get username (name)
	@Override
	public String getUsername() {
		return name;
	}

	// Check if the user account is not expired
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// Check if the user account is not locked
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// Check if user credentials are not expired
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Check if the user account is enabled
	@Override
	public boolean isEnabled() {
		return true;
	}
}
