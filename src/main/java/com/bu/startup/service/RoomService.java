package com.bu.startup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bu.startup.entity.Room;
import com.bu.startup.repo.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void updateRoomName(Long id, String name) {
        Room room = roomRepository.findById(id).orElseThrow();
        room.setName(name);
        roomRepository.save(room);
    }
}
