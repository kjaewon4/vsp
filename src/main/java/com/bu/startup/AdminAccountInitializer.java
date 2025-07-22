package com.bu.startup;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bu.startup.entity.User;
import com.bu.startup.entity.UserProfile;
import com.bu.startup.repo.UserProfileRepository;
import com.bu.startup.repo.UserRepository;

@Configuration
public class AdminAccountInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, 
                                       UserProfileRepository userProfileRepository,
                                       PasswordEncoder passwordEncoder) {
    	
    	return args -> {
    	    List<String[]> admins = List.of(
    	        new String[]{"admin1", "관리자1", "admin@example.com"},
    	        new String[]{"admin2", "관리자2", "super@example.com"},
    	        new String[]{"admin3", "관리자3", "manager@example.com"}
    	    );
    	    
    	    for (String[] adminData : admins) {
    	        String username = adminData[0];
    	        String nickname = adminData[1];
    	        String email = adminData[2];

    	        if (!userRepository.existsByUsername(username)) {
    	            User adminUser = new User();
    	            adminUser.setUsername(username);
    	            adminUser.setPassword(passwordEncoder.encode(username)); // 비밀번호를 아이디와 동일하게 설정
    	            userRepository.save(adminUser);

    	            UserProfile profile = new UserProfile();
    	            profile.setUser(adminUser);
    	            profile.setNickname(nickname);
    	            profile.setEmail(email);
    	            profile.setLevel(1);
    	            profile.setScore(0);
    	            profile.setBanned(false);
    	            profile.setAdmin(true);  // 관리자 권한
    	            userProfileRepository.save(profile);

    	            System.out.printf("✅ 관리자 계정(%s/%s)이 생성되었습니다.%n", username, username);
    	        }
    	    }
//        return args -> {
//            if (!userRepository.existsByUsername("admin")) {
//                User adminUser = new User();
//                adminUser.setUsername("admin");
//                adminUser.setPassword(passwordEncoder.encode("admin"));
//                userRepository.save(adminUser);
//
//                UserProfile profile = new UserProfile();
//                profile.setUser(adminUser);
//                profile.setNickname("관리자");
//                profile.setEmail("admin@example.com");
//                profile.setLevel(1);
//                profile.setScore(0);
//                profile.setBanned(false);
//                profile.setAdmin(true);  // 중요: 관리자 권한 부여
//                userProfileRepository.save(profile);
//
//                System.out.println("✅ 초기 관리자 계정(admin/admin)이 생성되었습니다.");
//            }
        };
    }
}
