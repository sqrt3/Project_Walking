package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;

import com.walking.project_walking.domain.userdto.UserSignUpDto;
import com.walking.project_walking.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public boolean checkEmailExists(String email) {
        return repository.existsByEmail(email);
    }

    public boolean checkNicknameExists(String nickname) {
        return repository.existsByNickname(nickname);
    }

    @Transactional
    public Users saveUser(UserSignUpDto dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        Users user = Users.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .name(dto.getName())
                .phone(dto.getPhone())
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .joinDate(LocalDate.now())
                .build();

        return repository.save(user);
    }
}