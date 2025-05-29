package com.meditourism.meditourism.clinic.controller;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity; // Aunque no se usa directamente en el controlador, se mantiene para contexto.
import com.meditourism.meditourism.clinic.service.ClinicService;
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
 * Controlador REST para la gestión de clínicas.
 * Proporciona endpoints para operaciones CRUD sobre las clínicas.
 */
@RestController
@RequestMapping("/clinics")
@Tag(name = "Clínicas", description = "API para la gestión de clínicas en Meditourism")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    /**
     * Obtiene una lista de todas las clínicas disponibles.
     * @return ResponseEntity con una lista de ClinicDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todas las clínicas",
            description = "Recupera una lista de todas las clínicas registradas en el sistema. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clínicas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    /**
     * Obtiene una clínica por su ID.
     * @param id El ID de la clínica a buscar.
     * @return ResponseEntity con el ClinicDTO de la clínica encontrada y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener clínica por ID",
            description = "Recupera los detalles de una clínica específica utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clínica encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))),
            @ApiResponse(responseCode = "404", description = "Clínica no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Clínica no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClinicDTO> getClinicById(
            @Parameter(description = "ID de la clínica a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    /**
     * Guarda una nueva clínica en el sistema. Requiere rol 'ADMIN'.
     * @param dto El ClinicDTO que contiene los datos de la nueva clínica.
     * @return ResponseEntity con el ClinicDTO de la clínica guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear una nueva clínica",
            description = "Permite a un usuario con rol 'ADMIN' registrar una nueva clínica en el sistema. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")) // Documenta que se requiere seguridad
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clínica creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"name\": \"El nombre no puede estar vacío\"}}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Una clínica con el mismo nombre o identificador ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Recurso ya existe\", \"message\": \"Ya existe una clínica con este nombre.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ClinicDTO> saveClinic(
            @Parameter(description = "Datos de la clínica a guardar. Los campos 'name', 'description', 'address', 'contactInfo' son obligatorios.", required = true,
                    schema = @Schema(implementation = ClinicDTO.class))
            @RequestBody @Valid ClinicDTO dto) {
        ClinicDTO savedClinic = clinicService.saveClinic(dto);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(savedClinic.getId())
                                .toUri())
                .body(savedClinic);
    }

    /**
     * Actualiza parcialmente una clínica existente. Requiere rol 'ADMIN'.
     * @param id El ID de la clínica a actualizar.
     * @param clinic El ClinicDTO con los campos a actualizar.
     * @return ResponseEntity con el ClinicDTO de la clínica actualizada y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar una clínica",
            description = "Permite a un usuario con rol 'ADMIN' actualizar los detalles de una clínica existente. Requiere autenticación con token JWT y rol de ADMIN. Se pueden actualizar uno o más campos.",
            security = @SecurityRequirement(name = "bearerAuth")) // Documenta que se requiere seguridad
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clínica actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Clínica no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Clínica no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ClinicDTO> updateClinic(
            @Parameter(description = "ID de la clínica a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de la clínica a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"name\": \"Nuevo Nombre\"}).", required = true,
                    schema = @Schema(implementation = ClinicDTO.class))
            @RequestBody ClinicDTO clinic) {
        return ResponseEntity.ok(clinicService.updateClinic(id, clinic));
    }

    /**
     * Elimina una clínica por su ID. Requiere rol 'ADMIN'.
     * @param id El ID de la clínica a eliminar.
     * @return ResponseEntity con el ClinicDTO de la clínica eliminada y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar una clínica por ID",
            description = "Permite a un usuario con rol 'ADMIN' eliminar una clínica del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")) // Documenta que se requiere seguridad
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clínica eliminada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Clínica no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Clínica no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ClinicDTO> deleteClinicById(
            @Parameter(description = "ID de la clínica a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(clinicService.deleteClinicById(id));
    }
}
