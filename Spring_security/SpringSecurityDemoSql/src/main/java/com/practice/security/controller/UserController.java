package com.practice.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.security.entity.UserInfo;
import com.practice.security.request.AuthRequest;
import com.practice.security.service.JwtService;
import com.practice.security.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private  UserInfoService service;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/welcome")
	public String welcome() {
		log.info("Access to the endpoint");
		return"Welcome this endpoint is not secure";
	}
	@PostMapping("/addNewUser")
	public String addNewUser(@RequestBody UserInfo userInfo)
	{
	 log.info("request for new user ");
	 return service.addUser(userInfo);
	}
	@GetMapping("/user/userProfile")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String userProfile(){
		log.info("User Profile Page");
		return "Welcome to User Profile";
	
	}
	@GetMapping("/admin/adminProfile")
	@PreAuthorize("hasAuthority('ROLE_Admin')")
	public String adminProfile(){
		log.info("Admin Profile Page");
		return "Welcome to Admin Profile";
	
	}

	@PostMapping("/generateToken")
	public String authenticateAndDetToken(@RequestBody AuthRequest authRequest)
	{
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if(authentication.isAuthenticated())
			{
			
			return jwtService.generateToken(authRequest.getUsername());
			
			
			}
		else
		{
			throw new  UsernameNotFoundException("Invalid user request !");
		}
			}
	

	
}
