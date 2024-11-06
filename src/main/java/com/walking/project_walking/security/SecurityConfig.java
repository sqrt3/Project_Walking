package com.walking.project_walking.security;

import com.walking.project_walking.handler.CustomAuthenticationSuccessHandler;
import com.walking.project_walking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(custom -> custom
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin", "/api/admin/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(custom -> custom
                        .loginPage("/auth/login")
                        .successHandler(new CustomAuthenticationSuccessHandler(userRepository))
                )
                .logout(custom -> custom
                        .logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
