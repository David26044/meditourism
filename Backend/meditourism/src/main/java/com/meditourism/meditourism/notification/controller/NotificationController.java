package com.meditourism.meditourism.notification.controller;

import com.meditourism.meditourism.notification.dto.NotificationRequestDTO;
import com.meditourism.meditourism.notification.dto.NotificationResponseDTO;
import com.meditourism.meditourism.notification.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> postNotification(@RequestBody @Valid NotificationRequestDTO dto) {
        NotificationResponseDTO savedNotification = notificationService.saveNotification(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNotification.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedNotification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> deleteNotificationById(@PathVariable Long id) {
        NotificationResponseDTO deletedNotification = notificationService.deleteNotificationById(id);
        return ResponseEntity.ok(deletedNotification);
    }
}
