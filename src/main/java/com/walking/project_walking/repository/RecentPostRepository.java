package com.walking.project_walking.repository;

import com.walking.project_walking.domain.RecentPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentPostRepository extends JpaRepository<RecentPost, Long> {
    Optional<RecentPost> findPostById(Long userId);
}
