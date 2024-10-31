package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserUpdate;
import com.walking.project_walking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Transactional
    public ResponseEntity<String> updateById(Long id, UserUpdate update) {
        Users users = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        users.update(update.getPassword(), update.getPhone(), update.getNickname(), update.getProfileImage());

        repository.save(users);

        return new ResponseEntity<>("수정이 완료되었습니다 :)", HttpStatus.OK);
    }


}
