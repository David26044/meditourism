package com.meditourism.meditourism.notification.service;

import com.meditourism.meditourism.notification.dto.NotificationRequestDTO;
import com.meditourism.meditourism.notification.dto.NotificationResponseDTO;

import java.util.List;

public interface INotificationService {

    NotificationResponseDTO getNotificationById(Long id);
    List<NotificationResponseDTO> getAllNotifications();
    List<NotificationResponseDTO> getAllNotificationsByUserId(Long userId);
    NotificationResponseDTO saveNotification(NotificationRequestDTO notificationRequestDTO);
    NotificationResponseDTO deleteNotificationById(Long id);

}
