package com.bu.startup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bu.startup.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {}
