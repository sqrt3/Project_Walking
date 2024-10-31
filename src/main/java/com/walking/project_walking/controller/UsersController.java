package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.service.UserService;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersController {
    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    // User 전체 조회
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findUsers() {
        List<UserResponse> list = service.findAll().stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    // User 한 명 조회
    @GetMapping("/users/{usersId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long usersId) {
        Users users = service.findById(usersId);
        return ResponseEntity.ok(new UserResponse(users));
    }

}
