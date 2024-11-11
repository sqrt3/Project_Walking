package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 헷갈림 주의, followingUser -> 팔로우 당하는 사용자 / followUser -> 팔로우 하는 사용자
    @Query("SELECT f FROM Follow f WHERE f.followUser.userId = :followerId")
    List<Follow> findFollowingByFollowerId(Long followerId);

    // 팔로워 조회 (자기 자신을 제외하고 조회)
    @Query("SELECT f FROM Follow f WHERE f.followingUser.userId = :followingUserId AND f.followUser.userId != :followingUserId")
    List<Follow> findFollowersExcludingSelf(@Param("followingUserId") Long followingUserId);

    // 팔로잉 수 카운트
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followUser.userId = :userId")
    Long countFollowing(Long userId);

    // 팔로워 수 카운트
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followingUser.userId = :userId")
    Long countFollowers(Long userId);

    // 팔로우 관계 확인
    Optional<Follow> findByFollowUserAndFollowingUser(Users followUser, Users followingUser);

}