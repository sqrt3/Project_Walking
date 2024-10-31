package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
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
    private Users user;
    private String redirectUri;

    public UserResponse(Users users) {
        id = users.getUserId();
        email = users.getEmail();
        password = users.getPassword();
        name = users.getName();
        phone = users.getPhone();
        nickname = users.getNickname();
        gender = users.getGender();
        birth = users.getBirth();
        joinDate = users.getJoinDate();
        lastLogin = users.getLastLogin();
        loginCount = users.getLoginCount();
        loginBrowser = users.getLoginBrowser();
        userLevel = users.getUserLevel();
        userExp = users.getUserExp();
        point = users.getPoint();
        isActive = users.getIsActive();
        profileImage = users.getProfileImage();
    }

    public UserResponse(Users user, String redirectUri) {
        this.user = user;
        this.redirectUri = redirectUri;
    }
}
