package com.walking.project_walking.repository;

import com.walking.project_walking.domain.PostImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostImagesRepository extends JpaRepository<PostImages, Long> {
    @Query("SELECT p.imageUrl FROM PostImages p WHERE p.postId = :postId")
    List<String> findImageUrlsByPostId(@Param("postId") Long postId);

    // postId와 연관된 모든 이미지 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM PostImages p WHERE p.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}

