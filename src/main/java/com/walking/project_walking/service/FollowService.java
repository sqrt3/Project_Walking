package com.walking.project_walking.service;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.followdto.FollowProfileDto;
import com.walking.project_walking.repository.FollowRepository;
import com.walking.project_walking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void followUser(Long followerId, Long followingId) {
        Users follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("팔로워를 찾을 수 없습니다."));
        Users following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("팔로우할 사용자를 찾을 수 없습니다."));

        Follow follow = new Follow();
        follow.setFollowUser(follower);
        follow.setFollowingUser(following);
        followRepository.save(follow);
    }

    // 팔로우 조회 (현재 사용자의 팔로잉 목록)
    public List<FollowProfileDto> getFollowing(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        return followRepository.findFollowingByFollowerId(userId);
    }

    // 팔로워 조회 (현재 사용자의 팔로워 목록)
    public List<FollowProfileDto> getFollower(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        return followRepository.findFollowersByFollowingId(userId);
    }

}
