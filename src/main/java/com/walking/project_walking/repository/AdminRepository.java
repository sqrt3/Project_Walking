package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Users, Long> {
    Users findByUserId(Long id);
}
