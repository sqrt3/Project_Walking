package com.walking.project_walking.domain.followdto;

import com.walking.project_walking.domain.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FollowerProfileDto {
    private Long userId;
    private final String nickname;
    private final String profileImage;

    // Follow 객체를 매개변수로 받는 생성자
    public FollowerProfileDto(Follow follow) {
        this.userId = follow.getFollowUser().getUserId();
        this.nickname = follow.getFollowUser().getNickname();
        this.profileImage = follow.getFollowUser().getProfileImage();
    }
}