package com.bu.startup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers(headers -> headers
                    .frameOptions().disable()  // H2 Console은 iframe 사용하므로 허용
                )
            .authorizeHttpRequests((authz) -> authz
            	.requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만
                .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll() 
                .requestMatchers("/api/assetbundles/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() 
                .anyRequest().authenticated()
               
            )
            .formLogin(form -> {
				try {
					form
					    .loginPage("/login")
					    .defaultSuccessUrl("/home", true).permitAll()
					.and()            
					.logout()
					    .logoutSuccessUrl("/login");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

