package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.domain.userdto.UserUpdate;
import com.walking.project_walking.service.UserService;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // User 정보 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<String> modifyUsersById(
            @PathVariable Long userId,
            @RequestBody  UserUpdate update
            ) {
        return service.updateById(userId, update);
    }

    // User Soft Delete
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUserById(
            @PathVariable Long userId
    ) {
        service.softDeleteUser(userId);
        return ResponseEntity.ok("사용자가 비활성화 되었습니다");
    }


}
