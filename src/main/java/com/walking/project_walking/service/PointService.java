package com.walking.project_walking.service;

import com.walking.project_walking.domain.PointLog;
import com.walking.project_walking.repository.PointLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointLogRepository pointLogRepository;

    @Transactional
    public void addPoints(Long userId, Integer amount, String description) {
        PointLog pointLog = new PointLog();
        pointLog.setUserId(userId);
        pointLog.setAmount(amount);
        pointLog.setDescription(description);

        pointLogRepository.save(pointLog);
    }

    @Transactional
    public void deductPoints(Long userId, Integer amount, String description) {
        PointLog pointLog = new PointLog();
        pointLog.setUserId(userId);
        pointLog.setAmount(-amount);
        pointLog.setDescription(description);

        pointLogRepository.save(pointLog);
    }
}
