package com.walking.project_walking.handler;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.enums.Point;
import com.walking.project_walking.repository.PointLogRepository;
import com.walking.project_walking.repository.UserRepository;
import com.walking.project_walking.service.PointService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final PointService pointService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String name = authentication.getName();
        Users user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        String userAgent = request.getHeader("User-Agent");
        user.setLoginCount(user.getLoginCount() + 1);

        if (user.getLastLogin().toLocalDate().isBefore(LocalDate.now())) {
            user.setPoint(user.getPoint() + Point.LOGIN_POINT.getAmount());
            pointService.addPoints(user.getUserId(), Point.LOGIN_POINT.getAmount(), "일일 로그인 보너스");
        }

        user.setLastLogin(LocalDateTime.now());
        user.setLoginBrowser(userAgent);
        userRepository.save(user);

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getUserId());

        response.sendRedirect("/");
    }
}
