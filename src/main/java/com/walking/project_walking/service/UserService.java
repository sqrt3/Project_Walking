package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.AddUserRequest;
import com.walking.project_walking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // 유저 정보 전체 조회
    public List<Users> findAll() {
        return repository.findAll();
    }

    // 유저 한 명 조회
    public Users findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    // 유저 정보 수정


}
