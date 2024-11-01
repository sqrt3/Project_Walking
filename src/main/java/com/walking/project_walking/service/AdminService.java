package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.AdminRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<Users> getAllUsers() {
        return adminRepository.findAll();
    }

    public Users findUser(Long id) {
        Users user = adminRepository.findByUserId(id);
        return user;
    }
}
