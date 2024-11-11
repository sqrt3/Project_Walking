package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p WHERE p.boardId = :boardId ORDER BY p.createdAt DESC, p.postId DESC")
    Page<Posts> findByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.boardId = ?1 AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', ?2, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', ?3, '%')) OR " +
            "p.userId = ?4) " +
            "ORDER BY p.createdAt DESC, p.postId DESC")
    Page<Posts> searchPosts(Long boardId, String title, String content, Long userId, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.weightValue >= :threshold")
    List<Posts> findHotPosts(@Param("threshold") double threshold);

    List<Posts> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.boardId = :boardId AND " +
            "(:title IS NULL OR p.title LIKE %:title%) AND " +
            "(:content IS NULL OR p.content LIKE %:content%) AND " +
            "(:userId IS NULL OR p.userId = :userId)")
    long countBySearchCriteria(@Param("boardId") Long boardId,
                                @Param("title") String title,
                                @Param("content") String content,
                                @Param("userId") Long userId);

    @Query("SELECT p.viewCount FROM Posts p WHERE p.postId = :postId")
    int getViewCountByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Posts p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
    void incrementViewCount(Long postId);

    // postId로 제목 찾기
    @Query("SELECT p.title FROM Posts p WHERE p.postId = :postId")
    String findTitleByPostId(@Param("postId") Long postId);
}