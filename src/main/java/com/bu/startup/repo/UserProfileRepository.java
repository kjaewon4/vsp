package com.bu.startup.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bu.startup.entity.User;
import com.bu.startup.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // 특정 User 엔티티로 프로필을 조회할 수 있도록
    Optional<UserProfile> findByUser(User user);

    // 이메일로도 조회 가능하게
    Optional<UserProfile> findByEmail(String email);
    
    
    
}
