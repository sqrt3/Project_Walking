package com.walking.project_walking.domain.followdto;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
public class FollowProfileDto {
    private final String nickname;
    private final String profileImage;
}
