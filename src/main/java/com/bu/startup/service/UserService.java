package com.bu.startup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bu.startup.entity.User;
import com.bu.startup.entity.UserProfile;
import com.bu.startup.repo.UserProfileRepository;
import com.bu.startup.repo.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String username, String password, String nickname, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        UserProfile profile = new UserProfile();
        profile.setNickname(nickname);
        profile.setEmail(email);
        profile.setLevel(1);
        profile.setScore(0);
        profile.setUser(user);

        user.setProfile(profile);
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

		   List<GrantedAuthority> authorities = new ArrayList<>();
		   if (profile.isAdmin()) {
		       authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		   }
		   else 
			   authorities.add(new SimpleGrantedAuthority("USER"));
   
   
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(),
            authorities
        );
    }
}