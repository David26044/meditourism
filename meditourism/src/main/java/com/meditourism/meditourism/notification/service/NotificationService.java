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

/**
 * Servicio para la gestión de notificaciones del sistema.
 * Permite crear, consultar y eliminar notificaciones para los usuarios.
 */
@Service
public class NotificationService implements INotificationService {

    @Autowired
    private IUserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthService authService;

    /**
     * Obtiene una notificación específica por su ID.
     *
     * @param id ID de la notificación a buscar
     * @return DTO con los datos de la notificación encontrada
     * @throws ResourceNotFoundException si no se encuentra la notificación
     */
    @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        return new NotificationResponseDTO(notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la notificación con ID: " + id)));
    }

    /**
     * Obtiene todas las notificaciones del sistema.
     *
     * @return Lista de todas las notificaciones en formato DTO
     */
    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        return NotificationResponseDTO.fromEntityList(notificationRepository.findAll());
    }

    /**
     * Obtiene todas las notificaciones de un usuario específico.
     *
     * @param userId ID del usuario cuyas notificaciones se quieren obtener
     * @return Lista de notificaciones del usuario en formato DTO
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public List<NotificationResponseDTO> getAllNotificationsByUserId(Long userId) {
        if(userService.getUserEntityById(userId) == null) {
            throw new ResourceNotFoundException("No existe el usuario con ID: " + userId);
        }
        return NotificationResponseDTO.fromEntityList(notificationRepository.findAllByUserId(userId));
    }

    /**
     * Crea una nueva notificación en el sistema.
     *
     * @param dto DTO con los datos de la notificación a crear
     * @return DTO con los datos de la notificación creada
     * @throws ResourceNotFoundException si no se encuentra el usuario destinatario
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
     * Elimina una notificación del sistema.
     *
     * @param id ID de la notificación a eliminar
     * @return DTO con los datos de la notificación eliminada
     * @throws ResourceNotFoundException si no se encuentra la notificación
     * @throws UnauthorizedAccessException si el usuario no tiene permisos para eliminar la notificación
     */
    @Override
    public NotificationResponseDTO deleteNotificationById(Long id) {
        NotificationEntity notificationEntity = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la notificación con ID: " + id));

        if (!authService.isOwnerOrAdmin(notificationEntity.getUser().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar esta notificación");
        }

        notificationRepository.delete(notificationEntity);
        return new NotificationResponseDTO(notificationEntity);
    }
}