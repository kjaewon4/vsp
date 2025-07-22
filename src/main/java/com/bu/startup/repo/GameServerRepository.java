package com.bu.startup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bu.startup.entity.GameServer;

public interface GameServerRepository extends JpaRepository<GameServer, Long> {
	List<GameServer> findAllByEnabledTrue();
}
