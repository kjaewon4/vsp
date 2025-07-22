package com.bu.startup.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String name;
    private int maxPlayers;
    private boolean active;
}