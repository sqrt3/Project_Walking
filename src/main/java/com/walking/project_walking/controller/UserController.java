package com.walking.project_walking.controller;

import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.PointLog;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.*;
import com.walking.project_walking.repository.UserRepository;
import com.walking.project_walking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponse> createUser(@ModelAttribute UserSignUpDto request) {
        Users user = userService.saveUser(request);
        String redirectUri = "/auth/login";

        UserResponse response = new UserResponse(user, redirectUri);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/auth/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        if (exists) {
            return ResponseEntity.badRequest().body("{\"error\": \"이미 사용 중인 이메일입니다.\"}");
        }
        return ResponseEntity.ok("{\"message\": \"사용 가능한 이메일입니다.\"}");
    }

    @GetMapping("/auth/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.checkNicknameExists(nickname);
        if (exists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @PostMapping("/auth/request-password-reset")
    public ResponseEntity<String> recoverPassword(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        if (!exists) {
            return ResponseEntity.badRequest().body("등록된 이메일이 아닙니다.");
        }
        userService.sendPasswordRecoveryEmail(email);

        return ResponseEntity.ok("비밀번호 재설정 이메일이 발송되었습니다.");
    }

//    @PostMapping("api/auth/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
//        try {
//            Users user = userService.findUserByToken(resetPasswordDto.getToken());
//            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
//            userRepository.save(user);
//            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
//        }
//    }

    // (Admin only) User 전체 조회
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findUsers() {
        List<UserResponse> list = userService.findAll().stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    // (Admin only) User 한 명 조회
    @GetMapping("/users/{usersId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long usersId) {
        Users users = userService.findById(usersId);
        return ResponseEntity.ok(new UserResponse(users));
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
    // todo 포인트 획득, 감소 시 코드상에서 처리?
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