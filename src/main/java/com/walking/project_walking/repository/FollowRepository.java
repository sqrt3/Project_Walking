package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Follow;
import com.walking.project_walking.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

}
