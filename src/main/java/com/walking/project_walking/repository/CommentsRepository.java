package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Comments;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

  @Query("SELECT COUNT(c) FROM Comments c WHERE c.postId = ?1 AND (c.isDeleted IS NULL OR c.isDeleted = false)")
  Integer countCommentsByPostId(Long postId);

  // 등록순 정렬
  List<Comments> findCommentsByPostIdOrderByCreatedAtAsc(Long postId);
}
