package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok annotations to automatically generate getters, setters, constructors, and toString methods
//This Lombok annotation automatically generates getter and setter methods for all fields, a toString method, an equals method, and a hashCode method.
@Data
// This Lombok annotation generates a constructor with all arguments.
@AllArgsConstructor
// This Lombok annotation generates a no-argument constructor.
@NoArgsConstructor
public class AuthRequest {

	// Fields to store username and password
	private String username;
	private String password;
}
