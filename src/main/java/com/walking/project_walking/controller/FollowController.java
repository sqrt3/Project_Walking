package com.walking.project_walking.controller;

import com.walking.project_walking.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequiredArgsConstructor
public class FollowController {
    private FollowService followService;

    // 사용자를 팔로우
    @PostMapping("/users/{followerId}/follow/{followingId}")
    public ResponseEntity<String> followUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    ) {
        followService.followUser(followerId, followingId);
        return ResponseEntity.ok("해당 유저를 팔로우 합니다.");
    }

}
