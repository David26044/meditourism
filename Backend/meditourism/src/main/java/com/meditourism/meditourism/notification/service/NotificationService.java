package com.meditourism.meditourism.notification.service;

import com.meditourism.meditourism.auth.service.AuthService;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.notification.dto.NotificationRequestDTO;
import com.meditourism.meditourism.notification.dto.NotificationResponseDTO;
import com.meditourism.meditourism.notification.entity.NotificationEntity;
import com.meditourism.meditourism.notification.repository.NotificationRepository;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private IUserService userService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuthService authService;

    @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        return new NotificationResponseDTO(notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la notificación con ID: " + id)));
    }

    /**
     * @return 
     */
    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
       return NotificationResponseDTO.fromEntityList(notificationRepository.findAll());
    }

    /**
     * @param userId 
     * @return
     */
    @Override
    public List<NotificationResponseDTO> getAllNotificationsByUserId(Long userId) {
        if(userService.getUserEntityById(userId) == null) {
            throw new ResourceNotFoundException("No existe el usuario con ID: " + userId);
        }
        return NotificationResponseDTO.fromEntityList(notificationRepository.findAllByUserId(userId));
    }

    /**
     * @param
     * @return
     */
    @Override
    public NotificationResponseDTO saveNotification(NotificationRequestDTO dto) {
        NotificationEntity notificationEntity = new NotificationEntity();
        UserEntity userEntity = userService.getUserEntityById(dto.getUserId());

        notificationEntity.setContent(dto.getContent());
        notificationEntity.setUser(userEntity);
        notificationEntity.setTitle(dto.getTitle());
        return new NotificationResponseDTO(notificationRepository.save(notificationEntity));
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public NotificationResponseDTO deleteNotificationById(Long id) {
        NotificationEntity notificationEntity = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la notificacion con ID: " + id));
        if (! authService.isOwnerOrAdmin(notificationEntity.getUser().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar esta reseña");
        }
        notificationRepository.delete(notificationEntity);
        return new NotificationResponseDTO(notificationEntity);
    }
}
