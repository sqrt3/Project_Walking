package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.repository.AdminRepository;
import com.walking.project_walking.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        List<Users> users = adminRepository.findAll();
        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse findUser(Long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserResponse(user);
    }
}
