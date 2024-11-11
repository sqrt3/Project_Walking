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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Users user = userService.findById(userId);
            model.addAttribute("user", user);
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

    @GetMapping("/auth/request-password-reset")
    public String requestPasswordReset() {
        return "request-password-reset";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/auth/login";
    }

    // 마이페이지 뷰
    @GetMapping("/myPage/{userId}")
    public String getMyPage(
            @PathVariable Long userId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // 세션에서 로그인한 사용자 ID를 가져옵니다.
        Long loggedInUserId = (Long) session.getAttribute("userId");

        // 로그인하지 않았으면 로그인 페이지로 리다이렉트
        if (loggedInUserId == null) {
            return "redirect:/auth/login";
        }

        // 로그인한 사용자 ID와 요청된 userId를 비교
        if (!loggedInUserId.equals(userId)) {
            // 에러 메시지 추가
            redirectAttributes.addFlashAttribute("error", "You do not have permission to access this page.");
            return "redirect:/";  // 메인 페이지로 리다이렉트
        }


        // 요청된 userId로 사용자 정보를 가져옵니다.
        Users user = userService.findById(userId);

        if (user != null) {
            model.addAttribute("user", user);
            return "myPageView";
        } else {
            return "redirect:/auth/login";
        }
    }

    // 유저 상세 페이지 뷰
    @GetMapping("/myPage/info/{userId}")
    public String getUserDetailPage(
            @PathVariable Long userId,
            HttpSession session,
            Model model
    ) {
        // 현재 로그인된 사용자 ID를 세션에서 가져오기
        Long currentUserId = (Long) session.getAttribute("userId");

        // 인증된 사용자 정보가 존재하는지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Users currentUser) {
            Users user = userService.findById(userId);

            // 모델에 사용자 정보와 현재 로그인된 사용자 ID를 추가
            model.addAttribute("user", user);  // 조회된 타 유저 정보
            model.addAttribute("currentUserId", currentUser.getUserId()); // 세션에서 가져온 현재 로그인된 사용자 ID
        }

        // 'followPage'는 타 유저 정보를 보여주는 페이지 이름
        return "followPage";
    }

}
