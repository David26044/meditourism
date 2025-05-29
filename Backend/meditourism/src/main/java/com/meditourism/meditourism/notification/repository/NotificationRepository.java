package com.meditourism.meditourism.notification.repository;

import com.meditourism.meditourism.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserId(Long id);

    Long id(Long id);
}
