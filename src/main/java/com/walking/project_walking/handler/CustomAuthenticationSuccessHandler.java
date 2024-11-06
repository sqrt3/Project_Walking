package com.walking.project_walking.handler;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String name = authentication.getName();
        Users user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        String userAgent = request.getHeader("User-Agent");
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLogin(LocalDateTime.now());
        user.setLoginBrowser(userAgent);
        userRepository.save(user);
        response.sendRedirect("/");
    }
}
