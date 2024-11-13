package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.BoardRequestDto;
import com.walking.project_walking.domain.dto.BoardResponseDto;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.service.AdminService;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/admin"))
public class AdminController {

  private final AdminService adminService;
  private final BoardService boardService;
  private final UserService userService;

  @GetMapping("/users")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = adminService.getAllUsers().stream().toList();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<UserResponse> findUser(@PathVariable Long userId) {
    UserResponse userResponse = adminService.findUser(userId);

    if (userResponse == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.ok(userResponse);
  }

  @PostMapping("/users/{userId}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
      @RequestParam String roleName) {
    userService.updateUserRole(userId, roleName);
    UserResponse userResponse = adminService.findUser(userId);
    return ResponseEntity.ok(userResponse);
  }

  @PostMapping("/boards")
  public ResponseEntity<BoardResponseDto> addBoard(@RequestBody BoardRequestDto boardRequestDto) {
    BoardResponseDto boardResponseDto = boardService.addBoard(boardRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
  }

  @DeleteMapping("/boards/{boardId}")
  public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
    BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
    if (boardResponseDto == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 ID를 가진 게시판이 없습니다.");
    }

    BoardResponseDto deletedBoard = boardService.deleteBoard(boardId);
    if (deletedBoard != null) {
      return ResponseEntity.ok().body(deletedBoard.getName() + " 게시판이 삭제 되었습니다.");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시판 삭제 실패");
    }
  }

  @PutMapping("/boards/{boardId}")
  public ResponseEntity<String> updateBoard(@PathVariable Long boardId,
      @RequestBody BoardRequestDto boardRequestDto) {
    BoardResponseDto updatedBoard = boardService.updateBoard(boardId, boardRequestDto);
    if (updatedBoard == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 ID를 가진 게시판이 없습니다.");
    }
    return ResponseEntity.ok().body(updatedBoard.getName() + " 게시판으로 변경 되었습니다.");
  }
}
