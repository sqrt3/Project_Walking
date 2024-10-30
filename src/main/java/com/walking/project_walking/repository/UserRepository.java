package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
