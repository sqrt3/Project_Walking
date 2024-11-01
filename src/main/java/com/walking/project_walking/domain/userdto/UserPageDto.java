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
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {
    private String nickname;
    private String email;
    private String name;
    private String profileImage;
    private List<FollowProfileDto> followers;
    private List<FollowProfileDto> following;

    public UserPageDto(Users user, List<FollowProfileDto> followers, List<FollowProfileDto> following) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
        this.followers = followers;
        this.following = following;
    }
}
