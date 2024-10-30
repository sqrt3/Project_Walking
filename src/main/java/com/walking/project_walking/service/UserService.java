package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.AddUserRequest;
import com.walking.project_walking.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // 유저 생성
    public Users saveUser(AddUserRequest request) {
        return repository.save(request.toEntity());
    }
}
