package com.walking.project_walking.domain.userdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserDetailDto {
    private final String nickname;
    private final String profileImage;
    private final Long followerCount;
    private final Long followingCount;
}
