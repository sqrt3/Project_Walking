package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.Users;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;
    private Boolean gender;
    private LocalDate birth;
    private LocalDate joinDate;
    private LocalDateTime lastLogin;
    private Long loginCount;
    private String loginBrowser;
    private Integer userLevel;
    private Long userExp;
    private Integer point;
    private Boolean isActive;
    private String profileImage;

    public Users toEntity() {
        return Users.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .phone(this.phone)
                .nickname(this.nickname)
                .gender(this.gender)
                .birth(this.birth)
                .joinDate(LocalDate.now())
                .lastLogin(LocalDateTime.now())
                .loginCount(0L)
                .loginBrowser(this.loginBrowser)
                .userLevel(1)
                .userExp(0L)
                .point(0)
                .isActive(true)
                .profileImage(this.profileImage)
                .build();
    }
}
