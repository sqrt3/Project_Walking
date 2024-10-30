package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.AdminRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Users> getAllUsers() {
        return adminRepository.findAll();
    }

    public Users findUser(Long id) {
        Users user = adminRepository.findByUserId(id);
        return user;
    }
}
