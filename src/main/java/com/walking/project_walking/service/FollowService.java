package com.walking.project_walking.service;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.FollowRepository;
import com.walking.project_walking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 사용자 팔로우 기능
    public ResponseEntity<String> followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("자기 자신은 팔로우할 수 없습니다.");
        }

        Users follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        Users following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우할 사용자를 찾을 수 없습니다."));

        // 이미 팔로우 중인지 확인
        if (followRepository.findByFollowUserAndFollowingUser(follower, following).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 팔로우 중입니다.");
        }

        Follow follow = new Follow();
        follow.setFollowUser(follower);
        follow.setFollowingUser(following);
        followRepository.save(follow);

        return ResponseEntity.ok("팔로우가 완료되었습니다.");
    }

    // 팔로우 취소 기능
    public ResponseEntity<String> unfollowUser(Long followerId, Long followingId) {
        Users follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        Users following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 취소할 사용자를 찾을 수 없습니다."));

        Follow follow = followRepository.findByFollowUserAndFollowingUser(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        followRepository.delete(follow);

        return ResponseEntity.ok("팔로우가 취소되었습니다.");
    }


    // 팔로우 조회 (현재 사용자의 팔로잉 목록)
    public List<Follow> getFollowing(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return followRepository.findFollowingByFollowerId(userId);
    }

    // 팔로워 조회 (현재 사용자의 팔로워 목록)
    public List<Follow> getFollower(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return followRepository.findFollowersByFollowingId(userId);
    }

}
