package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.followdto.FollowProfileDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class UserDetailDto {
    private final String nickname;
    private final String profileImage;
    private final Long followerCount;
    private final Long followingCount;
}
