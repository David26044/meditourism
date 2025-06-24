package com.meditourism.meditourism.contactForm.controller;

import com.meditourism.meditourism.contactForm.dto.ContactFormRequestDTO;
import com.meditourism.meditourism.contactForm.dto.ContactFormResponseDTO;
import com.meditourism.meditourism.contactForm.dto.TreatmentContactCountDTO;
import com.meditourism.meditourism.contactForm.service.ContactFormService;
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
 * Controlador REST para la gestión de formularios de contacto.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar formularios de contacto.
 */
@RestController
@RequestMapping("/contact-forms")
@Tag(name = "Formularios de Contacto", description = "API para la gestión de formularios de contacto en Meditourism")
public class ContactFormController {

    @Autowired
    private ContactFormService contactFormService;

    /**
     * Obtiene una lista de todos los formularios de contacto. Requiere rol 'ADMIN'.
     * @return ResponseEntity con una lista de ContactFormResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todos los formularios de contacto",
            description = "Recupera una lista de todos los formularios de contacto registrados en el sistema. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de formularios de contacto recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
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
    @GetMapping
    public ResponseEntity<List<ContactFormResponseDTO>> getAllContactForms() {
        return ResponseEntity.ok(contactFormService.getAllContactForms());
    }

    /**
     * Obtiene un formulario de contacto por su ID. Requiere rol 'ADMIN'.
     * @param id El ID del formulario de contacto a buscar.
     * @return ResponseEntity con el ContactFormResponseDTO del formulario encontrado y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener formulario de contacto por ID",
            description = "Recupera los detalles de un formulario de contacto específico utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario de contacto encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Formulario de contacto no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Formulario de contacto no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactFormResponseDTO> getContactFormById(
            @Parameter(description = "ID del formulario de contacto a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(contactFormService.getContactFormById(id));
    }

    /**
     * Obtiene todos los formularios de contacto asociados a un ID de tratamiento. Requiere rol 'ADMIN'.
     * @param treatmentId El ID del tratamiento.
     * @return ResponseEntity con una lista de ContactFormResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener formularios de contacto por ID de tratamiento",
            description = "Recupera una lista de formularios de contacto asociados a un tratamiento específico. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de formularios de contacto recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado o sin formularios de contacto asociados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"No se encontraron formularios de contacto para el tratamiento con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<List<ContactFormResponseDTO>> getAllByTreatmentId(
            @Parameter(description = "ID del tratamiento para filtrar los formularios de contacto", required = true, example = "1")
            @PathVariable Long treatmentId) {
        return ResponseEntity.ok(contactFormService.getAllContactFormsByTreatmentId(treatmentId));
    }

    /**
     * Obtiene todos los formularios de contacto asociados a un ID de usuario. Requiere rol 'ADMIN'.
     * @param userId El ID del usuario.
     * @return ResponseEntity con una lista de ContactFormResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener formularios de contacto por ID de usuario",
            description = "Recupera una lista de formularios de contacto enviados por un usuario específico. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de formularios de contacto recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o sin formularios de contacto asociados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"No se encontraron formularios de contacto para el usuario con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactFormResponseDTO>> getAllByUserId(
            @Parameter(description = "ID del usuario para filtrar los formularios de contacto", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(contactFormService.getContactFormByUserId(userId));
    }

    /**
     * Obtiene el conteo de formularios de contacto por tipo de tratamiento. Requiere rol 'ADMIN'.
     * @return ResponseEntity con una lista de TreatmentContactCountDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener conteo de formularios de contacto por tratamiento",
            description = "Recupera un listado del conteo de formularios de contacto agrupados por tipo de tratamiento. Útil para estadísticas. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo de formularios de contacto por tratamiento recuperado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentContactCountDTO.class))),
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/treatment-contact-count")
    public ResponseEntity<List<TreatmentContactCountDTO>> getTreatmentContactCounts() {
        return ResponseEntity.ok(contactFormService.getTreatmentContactCounts());
    }

    /**
     * Crea un nuevo formulario de contacto. Este endpoint es accesible públicamente.
     * @param dto El ContactFormRequestDTO que contiene los datos del nuevo formulario de contacto.
     * @return ResponseEntity con el ContactFormResponseDTO del formulario guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear un nuevo formulario de contacto",
            description = "Permite a cualquier usuario (público) enviar un nuevo formulario de contacto. Los campos 'name', 'email', 'message' y 'treatmentId' (opcional) son obligatorios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Formulario de contacto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"email\": \"El email no puede estar vacío\"}}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ContactFormResponseDTO> createContactForm(
            @Parameter(description = "Datos del formulario de contacto a guardar. Los campos 'name', 'email', 'message' son obligatorios. 'treatmentId' es opcional.", required = true,
                    schema = @Schema(implementation = ContactFormRequestDTO.class))
            @RequestBody @Valid ContactFormRequestDTO dto) {
        ContactFormResponseDTO savedContactForm = contactFormService.saveContactForm(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedContactForm.getId())
                        .toUri())
                .body(savedContactForm);
    }

    /**
     * Actualiza parcialmente un formulario de contacto existente. Requiere rol 'ADMIN'.
     * @param id El ID del formulario de contacto a actualizar.
     * @param dto El ContactFormRequestDTO con los campos a actualizar.
     * @return ResponseEntity con el ContactFormResponseDTO del formulario actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar un formulario de contacto",
            description = "Permite a un usuario con rol 'ADMIN' actualizar los detalles de un formulario de contacto existente. Requiere autenticación con token JWT y rol de ADMIN. Se pueden actualizar uno o más campos.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario de contacto actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Formulario de contacto no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Formulario de contacto no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ContactFormResponseDTO> updateContactForm(
            @Parameter(description = "ID del formulario de contacto a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del formulario de contacto a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"message\": \"Nuevo Mensaje\"}).", required = true,
                    schema = @Schema(implementation = ContactFormRequestDTO.class))
            @RequestBody ContactFormRequestDTO dto) {
        return ResponseEntity.ok(contactFormService.updateContactForm(id, dto));
    }

    /**
     * Elimina un formulario de contacto por su ID. Requiere rol 'ADMIN'.
     * @param id El ID del formulario de contacto a eliminar.
     * @return ResponseEntity con el ContactFormResponseDTO del formulario eliminado y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar un formulario de contacto por ID",
            description = "Permite a un usuario con rol 'ADMIN' eliminar un formulario de contacto del sistema utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario de contacto eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactFormResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Formulario de contacto no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Formulario de contacto no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ContactFormResponseDTO> deleteContactForm(
            @Parameter(description = "ID del formulario de contacto a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(contactFormService.deleteContactForm(id));
    }
}