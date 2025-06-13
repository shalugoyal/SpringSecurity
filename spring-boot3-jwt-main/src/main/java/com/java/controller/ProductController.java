package com.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.java.dto.AuthRequest;
import com.java.dto.Product;
import com.java.entity.UserInfo;
import com.java.service.JwtService;
import com.java.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	// Endpoint to welcome users, not secure
	@GetMapping("/welcome")
	public String welcome() {
		log.info("Accessed welcome endpoint.");
		return "Welcome, this endpoint is not secure.";
	}

	// Endpoint to add a new user
	@PostMapping("/new")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		log.info("Received request to add a new user.");
		return service.addUser(userInfo);
	}

	// Endpoint to retrieve all products, accessible only by users with ROLE_ADMIN
	// authority
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<Product> getAllTheProducts() {
		log.info("Retrieving all products.");
		return service.getProducts();
	}

	// Endpoint to retrieve a product by its ID, accessible only by users with
	// ROLE_USER authority
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Product getProductById(@PathVariable int id) {
		log.info("Retrieving product with ID: {}", id);
		return service.getProduct(id);
	}

	// Endpoint to authenticate user and generate JWT token
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		log.info("Authenticating user: {}", authRequest.getUsername());
		// Authenticate user using AuthenticationManager
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		// If authentication is successful, generate JWT token
		if (authentication.isAuthenticated()) {
			log.info("User authenticated successfully: {}", authRequest.getUsername());
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			// If authentication fails, throw UsernameNotFoundException
			log.error("Failed to authenticate user: {}", authRequest.getUsername());
			throw new UsernameNotFoundException("Invalid user credentials!");
		}
	}
}
