package com.userquestion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.userquestion.entities.User;

//User Entity's Repository to perform database operations
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
		
	boolean existsByEmail(String email);
	
	boolean existsByPassword(String password);
	
}