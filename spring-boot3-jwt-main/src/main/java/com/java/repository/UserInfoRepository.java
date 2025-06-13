package com.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.entity.UserInfo;

import java.util.Optional;

//JpaRepository interface provides methods for CRUD operations on UserInfo entities
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

	// Method to find a user by username
	Optional<UserInfo> findByName(String username);
}