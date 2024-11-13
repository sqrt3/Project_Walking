package com.walking.project_walking.repository;

import com.walking.project_walking.domain.PointLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {

  List<PointLog> findByUserId(Long userId);
}
