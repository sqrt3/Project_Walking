package com.walking.project_walking.repository;

import com.walking.project_walking.domain.LikeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLikeLogRepository extends JpaRepository<LikeLog, Long> {
    Optional<LikeLog> findByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
