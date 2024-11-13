package com.walking.project_walking.repository;

import com.walking.project_walking.domain.RecentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentPostRepository extends JpaRepository<RecentPost, Long> {

  // userId로 최근 본 postId 가져오기
  @Query("SELECT r.postId FROM RecentPost r WHERE r.userId = :userId")
  Long findPostIdByUserId(@Param("userId") Long userId);
}

