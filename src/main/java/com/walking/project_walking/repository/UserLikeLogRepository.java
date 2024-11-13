package com.walking.project_walking.repository;

import com.walking.project_walking.domain.LikeLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikeLogRepository extends JpaRepository<LikeLog, Long> {

  Optional<LikeLog> findByUserIdAndPostId(Long userId, Long postId);

  void deleteByUserIdAndPostId(Long userId, Long postId);
}
