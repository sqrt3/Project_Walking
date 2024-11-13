package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Role;
import com.walking.project_walking.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);

    Optional<Users> findByEmail(String email);

    @Query("SELECT u.userId FROM Users u WHERE u.nickname = :nickname")
    Long getUserIdByNickname(@Param("nickname") String nickname);

    @Query("SELECT u.nickname FROM Users u WHERE u.userId = :userId")
    String getNicknameByUserId(@Param("userId") Long userId);

    @Query("SELECT u.role FROM Users u WHERE u.userId = :userId")
    Role getRoleByUserId(@Param("userId") Long userId);
}
