package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.followdto.FollowProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 헷갈림 주의, followingUser -> 팔로우 당하는 사용자 / followUser -> 팔로우 하는 사용자
    // todo 경로 쓰는 것 말고 새로운 방법 검색해보기 왜이래
    @Query("SELECT new com.walking.project_walking.domain.followdto.FollowProfileDto" +
            "(f.followingUser.nickname, f.followingUser.profileImage) " +
            "FROM Follow f WHERE f.followUser.userId = :followerId")
    List<FollowProfileDto> findFollowingByFollowerId(Long followerId);

    // 헷갈림 주의, followingUser -> 팔로우 당하는 사용자 / followUser -> 팔로우 하는 사용자
    @Query("SELECT new com.walking.project_walking.domain.followdto.FollowProfileDto" +
            "(f.followUser.nickname, f.followUser.profileImage) " +
            "FROM Follow f WHERE f.followingUser.userId = :followingId")
    List<FollowProfileDto> findFollowersByFollowingId(Long followingId);
}