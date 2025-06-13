package com.java.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.java.entity.UserInfo;
import com.java.repository.UserInfoRepository;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

	// Autowire UserInfoRepository to retrieve user information from the database
	@Autowired
	private UserInfoRepository repository;

	// Method to load user details by username
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Retrieve user information from the database based on username
		Optional<UserInfo> userInfo = repository.findByName(username);

		// If user information is found, create UserDetails object using
		// UserInfoUserDetails class
		// Otherwise, throw UsernameNotFoundException
		return userInfo.map(UserInfoUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}
