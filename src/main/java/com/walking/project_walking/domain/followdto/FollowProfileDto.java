package com.walking.project_walking.domain.followdto;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.Users;
import lombok.*;

@RequiredArgsConstructor
@Getter
public class FollowProfileDto {
    private Long userId;
    private final String nickname;
    private final String profileImage;

    // Follow 객체를 매개변수로 받는 생성자
    public FollowProfileDto(Follow follow) {
        this.userId = follow.getFollowingUser().getUserId();
        this.nickname = follow.getFollowingUser().getNickname();
        this.profileImage = follow.getFollowingUser().getProfileImage();
    }

}
