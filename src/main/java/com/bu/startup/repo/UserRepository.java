package com.bu.startup.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bu.startup.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	
	boolean existsByUsername(String username);
	
}
