package com.bu.startup.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private boolean banned;
}