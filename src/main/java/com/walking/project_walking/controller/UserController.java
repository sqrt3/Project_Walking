package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.domain.userdto.UserSignUpDto;
import com.walking.project_walking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserController {
    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSignUpDto request) {
        Users user = userservice.saveUser(request);
        String redirectUri = "/auth/login";

        UserResponse response = new UserResponse(user, redirectUri);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}