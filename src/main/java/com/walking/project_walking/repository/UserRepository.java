package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
