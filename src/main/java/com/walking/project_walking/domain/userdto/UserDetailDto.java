package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.followdto.FollowProfileDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {
    private String nickname;
    private String profileImage;
    private List<FollowProfileDto> followers;
    private List<FollowProfileDto> following;
}
