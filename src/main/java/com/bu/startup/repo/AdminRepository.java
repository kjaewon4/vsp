package com.bu.startup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bu.startup.entity.Admin;
import com.bu.startup.entity.GameServer;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	
}
