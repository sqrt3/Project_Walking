package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Board;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.service.AdminService;
import com.walking.project_walking.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/admin"))
public class AdminController {
    private final AdminService adminService;
    private final BoardService boardService;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Users> findUser(@PathVariable Long userId) {
        Users user = adminService.findUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

//    @PostMapping("/boards")
//    public ResponseEntity<String> addBoard(@RequestBody Board board) {
//        Board newBoard = boardService.addBoard(board);
//        return ResponseEntity.ok().body(newBoard.getName() + " 게시판이 생성 되었습니다.");
//    }
//
//    @DeleteMapping("/boards/{boardId}")
//    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
//        Board board = boardService.getBoard(boardId);
//        if (board.getBoardId() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 ID를 가진 게시판이 없습니다.");
//        }
//        boardService.deleteBoard(board);
//        return ResponseEntity.ok().body(board.getName() + " 게시판이 삭제 되었습니다.");
//    }
}
