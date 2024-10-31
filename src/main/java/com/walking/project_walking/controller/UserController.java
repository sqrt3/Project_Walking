package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.domain.userdto.UserSignUpDto;
import com.walking.project_walking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/auth/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = userservice.checkEmailExists(email);
        if (exists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @GetMapping("/auth/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
        boolean exists = userservice.checkNicknameExists(nickname);
        if (exists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @PostMapping("/auth/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam String email) {
        boolean exists = userservice.checkEmailExists(email);
        if (!exists) {
            return ResponseEntity.badRequest().body("등록된 이메일이 아닙니다.");
        }

//        userservice.sendPasswordRecoveryEmail(email);

        return ResponseEntity.ok("비밀번호 재설정 이메일이 발송되었습니다.");
    }
}