package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.LoginUser;
import com.walking.project_walking.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(username);
        Users foundUser = user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new LoginUser(foundUser);
    }
}
