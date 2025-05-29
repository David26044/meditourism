package com.meditourism.meditourism.communityRule.controller;

import com.meditourism.meditourism.communityRule.dto.CommunityRuleRequestDTO;
import com.meditourism.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.meditourism.communityRule.service.ICommunityRuleService;
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
 * Controlador REST para la gestión de reglas de la comunidad.
 * Proporciona endpoints para operaciones CRUD sobre las reglas de la comunidad.
 */
@RestController
@RequestMapping("/community-rules")
@Tag(name = "Reglas de la Comunidad", description = "API para la gestión de reglas de la comunidad en Meditourism")
public class CommunityRuleController {

    @Autowired
    private ICommunityRuleService communityRuleService;

    /**
     * Obtiene una lista de todas las reglas de la comunidad disponibles.
     * @return ResponseEntity con una lista de CommunityRuleResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todas las reglas de la comunidad",
            description = "Recupera una lista de todas las reglas de la comunidad registradas en el sistema. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reglas de la comunidad recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommunityRuleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping
    public ResponseEntity<List<CommunityRuleResponseDTO>> getAllRules() {
        return ResponseEntity.ok(communityRuleService.getAllCommunityRules());
    }

    /**
     * Obtiene una regla de la comunidad por su ID.
     * @param id El ID de la regla de la comunidad a buscar.
     * @return ResponseEntity con el CommunityRuleResponseDTO de la regla encontrada y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener regla de la comunidad por ID",
            description = "Recupera los detalles de una regla de la comunidad específica utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Regla de la comunidad encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommunityRuleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Regla de la comunidad no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Regla de la comunidad no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommunityRuleResponseDTO> getRuleById(
            @Parameter(description = "ID de la regla de la comunidad a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(communityRuleService.getCommunityRuleById(id));
    }

    /**
     * Crea una nueva regla de la comunidad en el sistema. Requiere rol 'ADMIN'.
     * @param dto El CommunityRuleRequestDTO que contiene los datos de la nueva regla.
     * @return ResponseEntity con el CommunityRuleResponseDTO de la regla guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear una nueva regla de la comunidad",
            description = "Permite a un usuario con rol 'ADMIN' registrar una nueva regla para la comunidad. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Regla de la comunidad creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommunityRuleResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"name\": \"El nombre no puede estar vacío\"}}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Una regla con el mismo nombre ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Recurso ya existe\", \"message\": \"Ya existe una regla con este nombre.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CommunityRuleResponseDTO> createRule(
            @Parameter(description = "Datos de la regla de la comunidad a guardar. Los campos 'name' y 'description' son obligatorios.", required = true,
                    schema = @Schema(implementation = CommunityRuleRequestDTO.class))
            @RequestBody CommunityRuleRequestDTO dto) {
        CommunityRuleResponseDTO savedCommunityRule = communityRuleService.saveCommunityRule(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedCommunityRule.getId())
                        .toUri())
                .body(savedCommunityRule);
    }

    /**
     * Actualiza parcialmente una regla de la comunidad existente. Requiere rol 'ADMIN'.
     * @param id El ID de la regla de la comunidad a actualizar.
     * @param dto El CommunityRuleRequestDTO con los campos a actualizar.
     * @return ResponseEntity con el CommunityRuleResponseDTO de la regla actualizada y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar una regla de la comunidad",
            description = "Permite a un usuario con rol 'ADMIN' actualizar los detalles de una regla de la comunidad existente. Requiere autenticación con token JWT y rol de ADMIN. Se pueden actualizar uno o más campos.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Regla de la comunidad actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommunityRuleResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Regla de la comunidad no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Regla de la comunidad no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CommunityRuleResponseDTO> updateRule(
            @Parameter(description = "ID de la regla de la comunidad a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de la regla de la comunidad a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"description\": \"Nueva descripción\"}).", required = true,
                    schema = @Schema(implementation = CommunityRuleRequestDTO.class))
            @RequestBody CommunityRuleRequestDTO dto) {
        return ResponseEntity.ok(communityRuleService.updateCommunityRule(id, dto));
    }

    /**
     * Elimina una regla de la comunidad por su ID. Requiere rol 'ADMIN'.
     * @param id El ID de la regla de la comunidad a eliminar.
     * @return ResponseEntity con el CommunityRuleResponseDTO de la regla eliminada y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar una regla de la comunidad por ID",
            description = "Permite a un usuario con rol 'ADMIN' eliminar una regla de la comunidad del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Regla de la comunidad eliminada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommunityRuleResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Regla de la comunidad no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Regla de la comunidad no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommunityRuleResponseDTO> deleteRule(
            @Parameter(description = "ID de la regla de la comunidad a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(communityRuleService.deleteCommunityRule(id));
    }
}