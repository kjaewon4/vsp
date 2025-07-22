package com.bu.startup.service;

import org.springframework.stereotype.Service;

import com.bu.startup.entity.Admin;
import com.bu.startup.repo.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public Admin findByUsername(String username) {
        return adminRepository.findAll()
                .stream()
                .filter(admin -> admin.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
