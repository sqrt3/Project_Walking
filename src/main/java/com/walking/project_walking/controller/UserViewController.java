package com.walking.project_walking.controller;

import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserDetailDto;
import com.walking.project_walking.domain.userdto.UserPageDto;
import com.walking.project_walking.domain.userdto.UserPointLogDto;
import com.walking.project_walking.domain.userdto.UserUpdate;
import com.walking.project_walking.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserViewController {

    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
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
    @GetMapping("/mypage/{userId}")
    public String getMyPage(@PathVariable Long userId, HttpSession session, Model model) {
        session.setAttribute("userId", userId);

//        if (userId == null) {
//            // 세션에 userId가 없으면 로그인 페이지로 리다이렉트
//            return "redirect:/auth/login";
//        }

        // 세션에서 가져온 userId를 모델에 추가
        model.addAttribute("userId", userId);
        return "myPageView";
    }

    // 유저 상세 페이지 뷰
    @GetMapping("/info/{userId}")
    public String getUserDetailPage(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "followPage";
    }
}
