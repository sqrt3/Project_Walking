package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPageDto {
    private final Long userId;
    private final String nickname;
    private final String email;
    private final String name;
    private final String phone;
    private final String profileImage;
    private final Long followers;
    private final Long following;

    public UserPageDto(Users user, Long followers, Long following, String profileImage) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.profileImage = profileImage;
        this.followers = followers;
        this.following = following;
    }
}
