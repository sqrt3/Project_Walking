package com.walking.project_walking;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.UserRepository;
import com.walking.project_walking.service.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@Transactional
public class UsersRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserService userService;

    @Test
    @Commit
    void register() {
        Users user = Users.builder()
                .email("test@1111.com")
                .password(encoder.encode("test1234"))
                .name("테스트3")
                .gender(Boolean.FALSE)
                .birth(LocalDate.now())
                .joinDate(LocalDate.now())
                .nickname("테스트3")
                .phone("010-1111-2222")
                .build();

        Users savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void login() {
        Users user = userRepository.findByEmail("test@1234.com").get();
        Assertions.assertThat(user).isNotNull();
    }
}
