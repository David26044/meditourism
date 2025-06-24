package com.meditourism.meditourism.treatment.controller;

import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.service.ITreatmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
 * Controlador REST para la gestión de tratamientos.
 * Proporciona endpoints para operaciones CRUD sobre los tratamientos.
 */
@RestController
@RequestMapping("/treatments")
@Tag(name = "Tratamientos", description = "API para la gestión de tratamientos médicos en Meditourism")
public class TreatmentController {

    @Autowired
    ITreatmentService treatmentService;

    /**
     * Obtiene una lista de todos los tratamientos disponibles.
     * @return ResponseEntity con una lista de TreatmentDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todos los tratamientos",
            description = "Recupera una lista de todos los tratamientos registrados en el sistema. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tratamientos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> getAllTreatmets(){
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }

    /**
     * Obtiene un tratamiento por su ID.
     * @param id El ID del tratamiento a buscar.
     * @return ResponseEntity con el TreatmentDTO del tratamiento encontrado y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener tratamiento por ID",
            description = "Recupera los detalles de un tratamiento específico utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamiento encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Tratamiento no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentDTO> getTreatmentById(
            @Parameter(description = "ID del tratamiento a recuperar", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(treatmentService.getTreatmentById(id));
    }

    /**
     * Guarda un nuevo tratamiento en el sistema. Requiere rol 'ADMIN'.
     * @param treatment El TreatmentDTO que contiene los datos del nuevo tratamiento.
     * @return ResponseEntity con el TreatmentDTO del tratamiento guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear un nuevo tratamiento",
            description = "Permite a un usuario con rol 'ADMIN' registrar un nuevo tratamiento en el sistema. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tratamiento creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"name\": \"El nombre no puede estar vacío\"}}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Un tratamiento con el mismo nombre ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Recurso ya existe\", \"message\": \"Ya existe un tratamiento con este nombre.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TreatmentDTO> saveTreatment(
            @Parameter(description = "Datos del tratamiento a guardar. Los campos 'name', 'description' y 'price' son obligatorios.", required = true,
                    schema = @Schema(implementation = TreatmentDTO.class))
            @RequestBody @Valid TreatmentDTO treatment){
        TreatmentDTO savedTreatment = treatmentService.saveTreatment(treatment);
        URI location = ServletUriComponentsBuilder
                //Toma la infromacion de la URI de la solicitud actual, toma la URL de la solicitud que se está procesando
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTreatment.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedTreatment);
    }

    /**
     * Elimina un tratamiento por su ID. Requiere rol 'ADMIN'.
     * @param id El ID del tratamiento a eliminar.
     * @return ResponseEntity con el TreatmentDTO del tratamiento eliminado y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar un tratamiento por ID",
            description = "Permite a un usuario con rol 'ADMIN' eliminar un tratamiento del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamiento eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Tratamiento no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<TreatmentDTO> deleteTreatmentById(
            @Parameter(description = "ID del tratamiento a eliminar", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(treatmentService.deleteTreatmentById(id));
    }

    /**
     * Actualiza parcialmente un tratamiento existente. Requiere rol 'ADMIN'.
     * @param id El ID del tratamiento a actualizar.
     * @param dto El TreatmentDTO con los campos a actualizar.
     * @return ResponseEntity con el TreatmentDTO del tratamiento actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar un tratamiento",
            description = "Permite a un usuario con rol 'ADMIN' actualizar los detalles de un tratamiento existente. Requiere autenticación con token JWT y rol de ADMIN. Se pueden actualizar uno o más campos.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamiento actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Tratamiento no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<TreatmentDTO> updateTreatment(
            @Parameter(description = "ID del tratamiento a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del tratamiento a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"price\": 1500.00}).", required = true,
                    schema = @Schema(implementation = TreatmentDTO.class))
            @RequestBody TreatmentDTO dto){
        return ResponseEntity.ok(treatmentService.updateTreatment(id, dto));
    }
}