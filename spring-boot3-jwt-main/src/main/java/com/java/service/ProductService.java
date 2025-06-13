package com.java.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.dto.Product;
import com.java.entity.UserInfo;
import com.java.repository.UserInfoRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
@Service
public class ProductService {

	
	List<Product> productList = null;

	@Autowired
	private UserInfoRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Load products from the database during initialization
	@PostConstruct
	public void loadProductsFromDB() {
		log.info("Loading products from the database.");
		// Generate a list of sample products
		productList = IntStream
				.rangeClosed(1, 100).mapToObj(i -> Product.builder().productId(i).name("product " + i)
						.qty(new Random().nextInt(10)).price(new Random().nextInt(5000)).build())
				.collect(Collectors.toList());
		log.info("Products loaded successfully.");
	}

	// Get all products
	public List<Product> getProducts() {
		log.info("Retrieving all products.");
		return productList;
	}

	// Get product by ID
	public Product getProduct(int id) {
		log.info("Retrieving product with ID: {}", id);
		return productList.stream().filter(product -> product.getProductId() == id).findAny().orElseThrow(() -> {
			log.error("Product with ID {} not found.", id);
			return new RuntimeException("Product " + id + " not found");
		});
	}

	// Add a new user to the system
	public String addUser(UserInfo userInfo) {
		log.info("Adding new user to the system.");
		// Encrypt the user's password before saving
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		// Save the user information to the database
		repository.save(userInfo);
		log.info("User added successfully.");
		return "User added to the system";
	}
}
