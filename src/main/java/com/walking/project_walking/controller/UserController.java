package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.AddUserRequest;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody AddUserRequest request
    ) {
        Users users = service.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(users));
    }
}
