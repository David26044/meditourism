package com.meditourism.meditourism.user.controller;

import com.meditourism.meditourism.email.service.EmailService;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.jwt.JwtService;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity; // Aunque no se usa directamente en el controlador, se mantiene para contexto.
import com.meditourism.meditourism.user.repository.UserRepository; // Aunque no se usa directamente en el controlador, se mantiene para contexto.
import com.meditourism.meditourism.user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD sobre los usuarios, con algunos endpoints restringidos a roles de administrador.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios en Meditourism")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private EmailService emailService; // Inyectado pero no usado directamente en los métodos del controlador
    @Autowired
    private JwtService jwtService; // Inyectado pero no usado directamente en los métodos del controlador

    /**
     * Obtiene una lista de todos los usuarios registrados.
     * @return ResponseEntity con una lista de UserResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todos los usuarios",
            description = "Recupera una lista de todos los usuarios registrados en el sistema. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersResponseDTO());
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id El ID del usuario a buscar.
     * @return ResponseEntity con el UserResponseDTO del usuario encontrado y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener usuario por ID",
            description = "Recupera los detalles de un usuario específico utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID del usuario a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponseDTOById(id));

    }

    /**
     * Obtiene los detalles del usuario actualmente autenticado.
     * @param authentication El objeto Authentication de Spring Security.
     * @return ResponseEntity con el UserResponseDTO del usuario autenticado y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener detalles del usuario autenticado actual",
            description = "Recupera los detalles del perfil del usuario actualmente autenticado. Requiere un token JWT válido.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles del usuario recuperados exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyUser(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getMyUser(email));
    }


    /**
     * Guarda un nuevo usuario en el sistema.
     * @param dto El UserDTO que contiene los datos del nuevo usuario.
     * @return ResponseEntity con el UserResponseDTO del usuario guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear un nuevo usuario",
            description = "Permite registrar un nuevo usuario en el sistema. El registro de usuarios es público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"email\": \"El correo electrónico no puede estar vacío\"}}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Un usuario con el mismo correo electrónico ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Recurso ya existe\", \"message\": \"Ya existe un usuario con este correo electrónico.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(
            @Parameter(description = "Datos del usuario a guardar. Campos como 'email', 'password', 'firstName', 'lastName' son obligatorios.", required = true,
                    schema = @Schema(implementation = UserDTO.class))
            @RequestBody @Valid UserDTO dto) {
        UserResponseDTO savedUser = userService.saveUser(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedUser.getId())
                        .toUri())
                .body(savedUser);
    }

    /**
     * Actualiza la información de un usuario existente. Esto es típicamente para que un usuario actualice su propio perfil.
     * @param id El ID del usuario a actualizar.
     * @param dto El UserDTO con los campos a actualizar.
     * @param authenticate El objeto Authentication de Spring Security del usuario que realiza la actualización.
     * @return ResponseEntity con el UserResponseDTO del usuario actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar información del usuario",
            description = "Permite a un usuario autenticado actualizar su propia información de perfil. Requiere un token JWT válido.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autorizado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Prohibido. El usuario autenticado no está autorizado para actualizar a este usuario.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes permiso para realizar esta acción.\" }"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del usuario a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"firstName\": \"Nuevo Nombre\"}).", required = true,
                    schema = @Schema(implementation = UserDTO.class))
            @RequestBody UserDTO dto, Authentication authenticate){
        return ResponseEntity.ok(userService.updateUser(id, dto, authenticate));
    }

    /**
     * Elimina un usuario por su ID. Esto es típicamente para que un usuario elimine su propia cuenta.
     * @param id El ID del usuario a eliminar.
     * @param authenticate El objeto Authentication de Spring Security del usuario que realiza la eliminación.
     * @return ResponseEntity con el UserResponseDTO del usuario eliminado y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar usuario por ID",
            description = "Permite a un usuario autenticado eliminar su propia cuenta. Requiere un token JWT válido.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Prohibido. El usuario autenticado no está autorizado para eliminar a este usuario.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Access Denied\", \"message\": \"No tienes permiso para realizar esta acción.\" }"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUserById(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id, Authentication authenticate){
        return ResponseEntity.ok(userService.deleteUserById(id, authenticate));
    }

    // Nuevos endpoints de administración
    /**
     * Actualiza el rol de un usuario. Requiere el rol 'ADMIN'.
     * @param id El ID del usuario cuyo rol se va a actualizar.
     * @param roleId El ID del nuevo rol a asignar.
     * @param authentication El objeto Authentication de Spring Security del administrador que realiza la actualización.
     * @return ResponseEntity con el UserResponseDTO del usuario actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar rol de usuario (Solo Admin)",
            description = "Permite a un usuario con rol 'ADMIN' actualizar el rol de cualquier usuario. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol de usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario o Rol no encontrado con los IDs proporcionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario o Rol no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @Parameter(description = "ID del usuario cuyo rol se va a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del nuevo rol a asignar al usuario (ej. 1 para USER, 2 para ADMIN)", required = true, example = "1")
            @RequestParam Long roleId,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateUserRole(id, roleId, authentication));
    }

    /**
     * Permite a un administrador actualizar la información de cualquier usuario. Requiere el rol 'ADMIN'.
     * @param id El ID del usuario a actualizar.
     * @param dto El UserDTO con los campos a actualizar.
     * @param authentication El objeto Authentication de Spring Security del administrador que realiza la actualización.
     * @return ResponseEntity con el UserResponseDTO del usuario actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualización de usuario por administrador (Solo Admin)",
            description = "Permite a un usuario con rol 'ADMIN' actualizar la información de perfil de cualquier usuario. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente por el administrador",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autorizado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<UserResponseDTO> adminUpdateUser(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del usuario a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"firstName\": \"Nuevo Nombre\"}).", required = true,
                    schema = @Schema(implementation = UserDTO.class))
            @RequestBody UserDTO dto,
            Authentication authentication) {
        return ResponseEntity.ok(userService.adminUpdateUser(id, dto, authentication));
    }

    /**
     * Permite a un administrador eliminar cualquier usuario. Requiere el rol 'ADMIN'.
     * @param id El ID del usuario a eliminar.
     * @param authentication El objeto Authentication de Spring Security del administrador que realiza la eliminación.
     * @return ResponseEntity con el UserResponseDTO del usuario eliminado y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar usuario por administrador (Solo Admin)",
            description = "Permite a un usuario con rol 'ADMIN' eliminar cualquier usuario del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente por el administrador",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Usuario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<UserResponseDTO> adminDeleteUser(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(userService.adminDeleteUser(id, authentication));
    }
}