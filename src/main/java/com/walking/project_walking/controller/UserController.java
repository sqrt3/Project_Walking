package com.walking.project_walking.controller;

import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.PointLog;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.*;
import com.walking.project_walking.exception.UserNotFoundException;
import com.walking.project_walking.service.PasswordResetService;
import com.walking.project_walking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponse> createUser(@ModelAttribute UserSignUpDto request) {
        Users user = userService.saveUser(request);
        String redirectUri = "/auth/login";

        UserResponse response = new UserResponse(user, redirectUri);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUri)
                .body(response);
    }

    @GetMapping("/auth/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        if (exists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @GetMapping("/auth/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.checkNicknameExists(nickname);
        if (exists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    // 비밀번호 재설정
    @PostMapping("/auth/request-password-reset")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest) {
        String email = passwordResetRequest.getEmail();

        Map<String, String> response = new HashMap<>();

        try {
            String newPassword = passwordResetService.resetPassword(email);

            response.put("message", "새로운 비밀번호가 입력하신 메일로 전송되었습니다.");
            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            response.put("message", "이메일을 찾을 수 없습니다");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // User 정보 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<String> modifyUsersById(
            @PathVariable Long userId,
            @RequestBody UserUpdate update
    ) {
        return userService.updateById(userId, update);
    }

    // User Soft Delete
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUserById(
            @PathVariable Long userId
    ) {
        userService.softDeleteUser(userId);
        return ResponseEntity.ok("사용자가 비활성화 되었습니다");
    }

    // (User) 유저 조회
    @GetMapping("/users/{userId}/info")
    public ResponseEntity<UserDetailDto> getUserDetail(
            @PathVariable Long userId
    ) {
        UserDetailDto userDetail = userService.userDetail(userId);
        return ResponseEntity.ok(userDetail);
    }

    // myPage 조회
    @GetMapping("/users/{userId}/myPage")
    public ResponseEntity<UserPageDto> getMyPage(
            @PathVariable Long userId
    ) {
        UserPageDto userPageDto = userService.getInfo(userId);

        return ResponseEntity.ok(userPageDto);
    }

    // 유저 포인트 로그 조회
    @GetMapping("/users/{userId}/points")
    public ResponseEntity<List<UserPointLogDto>> getPointView(
            @PathVariable Long userId
    ) {
        List<PointLog> pointLogs = userService.getPointLog(userId);
        List<UserPointLogDto> userPointLogDtos = pointLogs.stream()
                .map(UserPointLogDto::new)
                .toList();
        return ResponseEntity.ok(userPointLogDtos);
    }

    // 유저 아이템 조회
    @GetMapping("/users/{userId}/items")
    public ResponseEntity<List<MyGoods>> getMyItems(
            @PathVariable Long userId
    ) {
        List<MyGoods> myGoods = userService.getGoods(userId);
        return ResponseEntity.ok(myGoods);
    }

    // 최근 게시물 조회
    @GetMapping("/users/{userId}/recent-post")
    public ResponseEntity<String> getRecentPost(@PathVariable Long userId) {
        String title = userService.getLastViewedPostTitle(userId);

        Map<String, String> response = new HashMap<>();
        if (title == null) {
            response.put("message", "최근 본 게시글이 없습니다.");
        } else {
            response.put("title", title);
        }

        return ResponseEntity.ok(title);
    }

}