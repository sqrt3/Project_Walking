package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.followdto.FollowProfileDto;
import com.walking.project_walking.domain.followdto.FollowerProfileDto;
import com.walking.project_walking.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FollowController {
    private final FollowService followService;

    // 팔로우/언팔로우 토글
    @PostMapping("/users/{followerId}/toggle-follow/{followingId}")
    public ResponseEntity<String> toggleFollowUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    ) {
        return followService.toggleFollowUser(followerId, followingId);
    }

    // 팔로잉을 조회 - 에러메시지 프로트에서 출력되도록 하기!
    @GetMapping("/users/{userId}/following")
    public ResponseEntity<List<FollowProfileDto>> getFollowing(
            @PathVariable Long userId
    ) {
        List<Follow> followings = followService.getFollowing(userId);

        List<FollowProfileDto> followingDto = followings.stream()
                .map(FollowProfileDto::new)
                .toList();

        return ResponseEntity.ok(followingDto);
    }

    // 팔로워를 조회 - "
    @GetMapping("/users/{userId}/follower")
    public ResponseEntity<List<FollowerProfileDto>> getFollower(
            @PathVariable Long userId
    ) {
        List<FollowerProfileDto> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    // 팔로잉 수 조회
    @GetMapping("/users/{userId}/count-following")
    public ResponseEntity<Long> getFollowingCount(
            @PathVariable Long userId
    ) {
        Long followingCount = followService.countFollowing(userId);
        return ResponseEntity.ok(followingCount);
    }

    // 팔로워 수 조회
    @GetMapping("/users/{userId}/count-followers")
    public ResponseEntity<Long> getFollowerCount(
            @PathVariable Long userId
    ) {
        Long followerCount = followService.countFollowers(userId);
        return ResponseEntity.ok(followerCount);
    }

}
