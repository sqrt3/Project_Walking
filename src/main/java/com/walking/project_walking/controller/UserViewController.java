package com.walking.project_walking.controller;

import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.userdto.UserDetailDto;
import com.walking.project_walking.domain.userdto.UserPageDto;
import com.walking.project_walking.domain.userdto.UserPointLogDto;
import com.walking.project_walking.domain.userdto.UserUpdate;
import com.walking.project_walking.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

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
    // Principal - Spring Security에서 제공해주는 인터페이스
    // 주로 현재 인증된 사용자에 대한 식별 정보를 담고 있음. (userId를 가져올 수 있음)
    @GetMapping("/mypage/{userId}")
    public String getMyPage(@PathVariable Long userId, Model model) {
//        Long userId = Long.valueOf(principal.getName());

        // 사용자 기본 정보 조회
        UserPageDto userPageDto = userService.getInfo(userId);
        model.addAttribute("currentUserInfo", userPageDto);

        // 수정할 사용자 정보를 담음
        model.addAttribute("userUpdate", new UserUpdate());

        // 사용자 아이템 목록 조회
        List<MyGoods> myItems = userService.getGoods(userId);
        model.addAttribute("myItems", myItems);

        // 사용자 포인트 로그 조회
        List<UserPointLogDto> pointLogs = userService.getPointLog(userId).stream()
                .map(UserPointLogDto::new)
                .toList();
        model.addAttribute("pointLogs", pointLogs);

        // 팔로잉, 팔로워 조회

        return "myPageView";
    }
}
