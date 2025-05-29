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

// Swagger/OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador para la gestión de notificaciones.
 * Proporciona endpoints para operaciones CRUD y consultas sobre notificaciones.
 */
@RestController
@RequestMapping("/notifications")
@Tag(name = "Notifications", description = "API para la gestión de notificaciones en Meditourism")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    /**
     * Obtiene una notificación por su ID.
     * @param id ID de la notificación a buscar
     * @return ResponseEntity con la notificación encontrada
     */
    @Operation(summary = "Obtener notificación por ID",
            description = "Recupera los detalles de una notificación específica utilizando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"Notificación no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(
            @Parameter(description = "ID de la notificación a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    /**
     * Obtiene todas las notificaciones del sistema.
     * @return ResponseEntity con lista de todas las notificaciones
     */
    @Operation(summary = "Obtener todas las notificaciones",
            description = "Recupera una lista de todas las notificaciones registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene todas las notificaciones de un usuario específico.
     * @param userId ID del usuario cuyas notificaciones se quieren recuperar
     * @return ResponseEntity con lista de notificaciones del usuario
     */
    @Operation(summary = "Obtener notificaciones por ID de usuario",
            description = "Recupera todas las notificaciones asociadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o sin notificaciones",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotificationsByUserId(
            @Parameter(description = "ID del usuario para el cual se desean recuperar las notificaciones", required = true, example = "1")
            @PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Crea una nueva notificación.
     * @param dto Datos de la notificación a crear
     * @return ResponseEntity con la notificación creada y ubicación del nuevo recurso
     */
    @Operation(summary = "Crear nueva notificación",
            description = "Registra una nueva notificación en el sistema",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "No autorizado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> postNotification(
            @Parameter(description = "Datos de la notificación a crear", required = true,
                    schema = @Schema(implementation = NotificationRequestDTO.class))
            @RequestBody @Valid NotificationRequestDTO dto) {
        NotificationResponseDTO savedNotification = notificationService.saveNotification(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNotification.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedNotification);
    }

    /**
     * Elimina una notificación por su ID.
     * @param id ID de la notificación a eliminar
     * @return ResponseEntity con la notificación eliminada
     */
    @Operation(summary = "Eliminar notificación",
            description = "Elimina una notificación específica del sistema utilizando su ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación eliminada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Prohibido - Sin permisos suficientes",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> deleteNotificationById(
            @Parameter(description = "ID de la notificación a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        NotificationResponseDTO deletedNotification = notificationService.deleteNotificationById(id);
        return ResponseEntity.ok(deletedNotification);
    }
}