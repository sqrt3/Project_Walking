package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.UserRepository;
import com.walking.project_walking.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardViewController {
    private final UserService userService;
    private final UserRepository userRepository;
    private static final List<Long> NoNoticeBoards = Arrays.asList(1L, 2L, 3L);

    @GetMapping("/boardList")
    public String getBoard2(@RequestParam(defaultValue = "1") Long boardId,
                            HttpSession session,
                            Model model) {

        Long userId = (Long) session.getAttribute("userId");

        Users user = null;
        String userNickname = null;
        if (userId != null) {
            user = userService.findById(userId);
            userNickname = userRepository.getNicknameByUserId(userId);
        }

        model.addAttribute("boardId", boardId);
        model.addAttribute("noNotice", NoNoticeBoards.contains(boardId));
        model.addAttribute("user", user);
        model.addAttribute("userNickname", userNickname);
        return "board-list";
    }
}
