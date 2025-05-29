package com.meditourism.meditourism.role.controller;

import com.meditourism.meditourism.role.entity.RoleEntity;
import com.meditourism.meditourism.role.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.security.access.prepost.PreAuthorize; // Para documentar la seguridad de los endpoints

/**
 * Controlador REST para la gestión de roles de usuario.
 * Proporciona endpoints para operaciones CRUD sobre los roles.
 */
@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "API para la gestión de roles de usuario en Meditourism")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * Obtiene un rol por su ID.
     * @param id El ID del rol a buscar.
     * @return ResponseEntity con el RoleEntity del rol encontrado y estado HTTP 200 OK, o 404 Not Found si no existe.
     */
    @Operation(summary = "Obtener un rol por ID",
            description = "Recupera los detalles de un rol específico utilizando su ID. Este endpoint está restringido a usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleEntity.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene los permisos necesarios.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Access Denied\"}"))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Rol no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth") // Documenta que se requiere seguridad
    @PreAuthorize("hasRole('ADMIN')") // Asumiendo que solo los ADMIN pueden ver roles específicos por ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleEntity> getRoleById(
            @Parameter(description = "ID del rol a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        RoleEntity role = roleService.getRoleById(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtiene una lista de todos los roles disponibles.
     * @return ResponseEntity con una lista de RoleEntity y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todos los roles",
            description = "Recupera una lista de todos los roles registrados en el sistema. Este endpoint está restringido a usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleEntity.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene los permisos necesarios.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Access Denied\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')") // Asumiendo que solo los ADMIN pueden ver todos los roles
    @GetMapping
    public ResponseEntity<List<RoleEntity>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    /**
     * Crea un nuevo rol en el sistema. Requiere rol 'ADMIN'.
     * @param role El RoleEntity que contiene los datos del nuevo rol.
     * @return ResponseEntity con el RoleEntity del rol guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear un nuevo rol",
            description = "Permite a un usuario con rol 'ADMIN' registrar un nuevo rol en el sistema. Requiere autenticación con token JWT y rol de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"El nombre del rol no puede estar vacío\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Un rol con el mismo nombre ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Conflict\", \"message\": \"Ya existe un rol con este nombre.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoleEntity> saveRole(
            @Parameter(description = "Datos del rol a guardar. El campo 'name' es obligatorio.", required = true,
                    schema = @Schema(implementation = RoleEntity.class))
            @RequestBody RoleEntity role) {
        RoleEntity savedRole = roleService.saveRole(role);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedRole.getId())
                        .toUri())
                .body(savedRole);
    }

    /**
     * Elimina un rol por su ID. Requiere rol 'ADMIN'.
     * @param id El ID del rol a eliminar.
     * @return ResponseEntity con el RoleEntity del rol eliminado y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar un rol por ID",
            description = "Permite a un usuario con rol 'ADMIN' eliminar un rol del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleEntity.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Rol no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<RoleEntity> deleteRoleById(
            @Parameter(description = "ID del rol a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(roleService.deleteRoleById(id));
    }

    /**
     * Actualiza un rol existente. Requiere rol 'ADMIN'.
     * @param role El RoleEntity con los datos actualizados del rol.
     * @return ResponseEntity con el RoleEntity del rol actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar un rol",
            description = "Permite a un usuario con rol 'ADMIN' actualizar los detalles de un rol existente. Requiere autenticación con token JWT y rol de ADMIN. Se pueden actualizar uno o más campos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"El nombre del rol no puede estar vacío\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Rol no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Un rol con el mismo nombre ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Conflict\", \"message\": \"Ya existe un rol con este nombre.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<RoleEntity> updateRole(
            @Parameter(description = "Datos del rol a actualizar. Se requiere el 'id' del rol y se pueden modificar otros campos como 'name'.", required = true,
                    schema = @Schema(implementation = RoleEntity.class))
            @RequestBody RoleEntity role) {
        return ResponseEntity.ok(roleService.updateRole(role));
    }
}
