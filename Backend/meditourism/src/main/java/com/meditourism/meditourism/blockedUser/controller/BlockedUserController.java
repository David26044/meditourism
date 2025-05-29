package com.meditourism.meditourism.blockedUser.controller;

import com.meditourism.meditourism.blockedUser.dto.BlockedUserRequestDTO;
import com.meditourism.meditourism.blockedUser.dto.BlockedUserResponseDTO;
import com.meditourism.meditourism.blockedUser.service.IBlockedUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

// Importaciones de Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Para documentar la seguridad

/**
 * Controlador REST para la gestión de usuarios bloqueados.
 * Proporciona endpoints para operaciones CRUD sobre los usuarios bloqueados.
 */
@RestController
@RequestMapping("/blocked-users")
@Tag(name = "Usuarios Bloqueados", description = "API para la gestión de usuarios bloqueados en Meditourism")
public class BlockedUserController {

    @Autowired
    private IBlockedUserService blockedUserService;

    /**
     * Obtiene una lista de todos los usuarios bloqueados. Requiere rol 'ADMIN'.
     * @return ResponseEntity con una lista de BlockedUserResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todos los usuarios bloqueados",
            description = "Recupera una lista de todos los usuarios bloqueados en el sistema. Este endpoint requiere autenticación con token JWT y rol de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios bloqueados recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlockedUserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth") // Documenta que se requiere seguridad
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BlockedUserResponseDTO>> getAllBlockedUsers() {
        return ResponseEntity.ok(blockedUserService.getAllBlockedUsers());
    }

    /**
     * Obtiene un usuario bloqueado por su ID. Requiere rol 'ADMIN'.
     * @param id El ID del usuario bloqueado a buscar.
     * @return ResponseEntity con el BlockedUserResponseDTO del usuario bloqueado encontrado y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener usuario bloqueado por ID",
            description = "Recupera los detalles de un usuario bloqueado específico utilizando su ID. Este endpoint requiere autenticación con token JWT y rol de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario bloqueado encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlockedUserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario bloqueado no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario bloqueado no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BlockedUserResponseDTO> getBlockedUserById(
            @Parameter(description = "ID del usuario bloqueado a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(blockedUserService.findBlockedUserByUserId(id));
    }

    /**
     * Guarda un nuevo usuario bloqueado en el sistema. Requiere rol 'ADMIN'.
     * @param dto El BlockedUserRequestDTO que contiene los datos del nuevo usuario bloqueado.
     * @return ResponseEntity con el BlockedUserResponseDTO del usuario bloqueado guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Bloquear un usuario",
            description = "Permite a un usuario con rol 'ADMIN' bloquear un usuario en el sistema. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario bloqueado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlockedUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"userId\": \"El ID de usuario no puede ser nulo\"}}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. El usuario ya se encuentra bloqueado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Recurso ya existe\", \"message\": \"El usuario con ID 123 ya está bloqueado.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BlockedUserResponseDTO> postBlockedUser(
            @Parameter(description = "Datos del usuario a bloquear. Se espera el 'userId' del usuario a bloquear y opcionalmente un 'reason'.", required = true,
                    schema = @Schema(implementation = BlockedUserRequestDTO.class))
            @RequestBody @Valid BlockedUserRequestDTO dto) {
        BlockedUserResponseDTO savedBlockedUser = blockedUserService.saveBlockedUser(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedBlockedUser.getId())
                        .toUri())
                .body(savedBlockedUser);
    }

    /**
     * Actualiza un usuario bloqueado existente. Requiere rol 'ADMIN'.
     * @param id El ID del usuario bloqueado a actualizar.
     * @param dto El BlockedUserRequestDTO con los campos a actualizar (ej. la razón del bloqueo).
     * @return ResponseEntity con el BlockedUserResponseDTO del usuario bloqueado actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar un usuario bloqueado",
            description = "Permite a un usuario con rol 'ADMIN' actualizar los detalles de un usuario bloqueado existente (ej. la razón del bloqueo). Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario bloqueado actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlockedUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario bloqueado no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario bloqueado no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BlockedUserResponseDTO> patchBlockedUser(
            @Parameter(description = "ID del usuario bloqueado a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del usuario bloqueado a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"reason\": \"Nueva razón\"}).", required = true,
                    schema = @Schema(implementation = BlockedUserRequestDTO.class))
            @RequestBody BlockedUserRequestDTO dto) {
        return ResponseEntity.ok(blockedUserService.updateBlockedUser(id, dto));
    }

    /**
     * Elimina un usuario bloqueado por su ID. Requiere rol 'ADMIN'.
     * @param id El ID del usuario bloqueado a eliminar.
     * @return ResponseEntity con el BlockedUserResponseDTO del usuario desbloqueado y estado HTTP 200 OK.
     */
    @Operation(summary = "Desbloquear un usuario por ID",
            description = "Permite a un usuario con rol 'ADMIN' desbloquear un usuario del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario desbloqueado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlockedUserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario bloqueado no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario bloqueado no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BlockedUserResponseDTO> deleteBlockedUser(
            @Parameter(description = "ID del usuario bloqueado a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(blockedUserService.deleteBlockedUser(id));
    }
}
