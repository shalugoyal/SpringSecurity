package com.practice.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class UserInfo {
	   @Id
	    @GeneratedValue(strategy = GenerationType.AUTO) 
	   @Column(name = "ID")
	    private long id; 
	    private String name; 
	    private String email; 
	    
	    private String password; 
	    private String roles; 
	

}
