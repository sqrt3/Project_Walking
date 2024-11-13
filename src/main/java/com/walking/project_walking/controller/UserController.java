package com.walking.project_walking.controller;

import com.walking.project_walking.domain.PointLog;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.PasswordResetRequest;
import com.walking.project_walking.domain.userdto.UserDetailDto;
import com.walking.project_walking.domain.userdto.UserGoodsDto;
import com.walking.project_walking.domain.userdto.UserPageDto;
import com.walking.project_walking.domain.userdto.UserPointLogDto;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.domain.userdto.UserSignUpDto;
import com.walking.project_walking.domain.userdto.UserUpdate;
import com.walking.project_walking.exception.UserNotFoundException;
import com.walking.project_walking.service.PasswordResetService;
import com.walking.project_walking.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final PasswordResetService passwordResetService;

  @PostMapping("/auth/login")
  public String login(@RequestParam String email, @RequestParam String password, Model model) {
    boolean isValid = userService.validateLogin(email, password);

    if (!isValid) {
      return "redirect:/auth/login?error=true";
    }
    return "redirect:/dashboard";
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<UserResponse> createUser(@ModelAttribute UserSignUpDto request) {
    Users user = userService.saveUser(request);

    UserResponse response = new UserResponse(user, "회원가입이 완료되었습니다.");

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/auth/check-email")
  public ResponseEntity<String> checkEmail(@RequestParam String email) {
    boolean exists = userService.checkEmailExists(email);
    if (exists) {
      return ResponseEntity.badRequest().body("{\"error\": \"이미 사용 중인 이메일 입니다.\"}");
    }
    return ResponseEntity.ok("{\"message\": \"사용 가능한 이메일입니다.\"}");
  }

  @GetMapping("/auth/check-nickname")
  public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
    boolean exists = userService.checkNicknameExists(nickname);
    if (exists) {
      return ResponseEntity.badRequest().body("{\"error\": \"이미 사용 중인 닉네임 입니다.\"}");
    }
    return ResponseEntity.ok("{\"message\": \"사용 가능한 닉네임 입니다.\"}");
  }

  @GetMapping("/auth/check-phone")
  public ResponseEntity<String> checkPhone(@RequestParam String phone) {
    boolean exists = userService.checkPhoneExists(phone);
    if (exists) {
      return ResponseEntity.badRequest().body("{\"error\": \"이미 사용 중인 휴대전화 번호 입니다.\"}");
    }
    return ResponseEntity.ok("{\"message\": \"사용 가능한 휴대전화 번호 입니다.\"}");
  }

  // 비밀번호 재설정
  @PostMapping("/auth/request-password-reset")
  public ResponseEntity<Map<String, String>> requestPasswordReset(
      @RequestBody PasswordResetRequest passwordResetRequest) {
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
  public ResponseEntity<?> modifyUsersById(
      @PathVariable Long userId,
      @RequestPart(value = "update", required = false) UserUpdate update,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
  ) {
    return userService.updateById(userId, update, profileImage);
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
  public ResponseEntity<List<UserGoodsDto>> getMyItems(
      @PathVariable Long userId
  ) {
    List<UserGoodsDto> myGoods = userService.getGoods(userId);
    return ResponseEntity.ok(myGoods);
  }

  // 아이템 사용
  @PostMapping("/{userId}/items/{goodsId}/use")
  public ResponseEntity<String> useItem(
      @PathVariable Long userId,
      @PathVariable Long goodsId
  ) {
    userService.useItem(userId, goodsId);
    return ResponseEntity.ok("아이템 사용 완료");
  }

  // 최근 게시물 조회
  @GetMapping("/users/{userId}/recent-post")
  public ResponseEntity<Map<String, Object>> getRecentPost(@PathVariable Long userId) {
    Long postId = userService.getLastViewedPostId(userId); // postId는 Long 타입
    String title = userService.getLastViewedPostTitle(userId);

    Map<String, Object> response = new HashMap<>();
    if (title == null) {
      response.put("message", "최근 본 게시글이 없습니다."); // String 타입 message
    } else {
      response.put("postId", postId); // Long 타입 postId
      response.put("title", title);
    }

    return ResponseEntity.ok(response);
  }
}