package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Users currentUser) {
            Users user = userService.findById(currentUser.getUserId());
            model.addAttribute("user", user);

            session.setAttribute("userId", user.getUserId());
        }

        return "index";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/auth/login";
    }

    // 마이페이지 뷰
    @GetMapping("/myPage/{userId}")
    public String getMyPage(@PathVariable Long userId, HttpSession session, Model model) {
        // 요청된 userId로 사용자 정보를 가져옵니다.
        Users user = userService.findById(userId);

        if (user != null) {
            model.addAttribute("userId", userId);
            return "myPageView";
        } else {
            return "redirect:/auth/login";
        }
    }

    // 유저 상세 페이지 뷰
    @GetMapping("/myPage/info/{userId}")
    public String getUserDetailPage(@PathVariable Long userId, Model model) {
        Users user = userService.findById(userId);

        if (user != null) {
            model.addAttribute("userId", userId);
            model.addAttribute("user", user);
        }

        return "followPage";
    }
}
