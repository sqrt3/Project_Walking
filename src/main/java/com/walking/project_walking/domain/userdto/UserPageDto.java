package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.followdto.FollowProfileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserPageDto {
    private final String nickname;
    private final String email;
    private final String name;
    private final String phone;
    private final String profileImage;
    private final Long followers;
    private final Long following;

    public UserPageDto(Users user, Long followers, Long following) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.profileImage = user.getProfileImage();
        this.followers = followers;
        this.following = following;
    }
}
