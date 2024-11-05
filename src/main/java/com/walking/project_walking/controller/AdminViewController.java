package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.dto.BoardResponseDto;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final UserService userService;
    private final BoardService boardService;

    @GetMapping
    public String adminView(Model model) {
        model.addAttribute("user", userService.findById(2L)); // 테스트용, 삭제 필요
        return "admin";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<Users> usersList = userService.findAll().stream().toList();
        model.addAttribute("userList", usersList);
        return "users";
    }

    @GetMapping("/users/{userId}")
    public String manageUser(@PathVariable Long userId, Model model) {
        Users user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/board")
    public String manageBoard(Model model) {
        List<BoardResponseDto> boardList = boardService.getBoardsList();
        model.addAttribute("boardList", boardList);
        return "board";
    }
}
