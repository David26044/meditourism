package com.meditourism.meditourism.review.controller;

import com.meditourism.meditourism.review.dto.ReviewRequestDTO;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.service.IReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
 * Controlador REST para la gestión de reseñas.
 * Proporciona endpoints para operaciones CRUD sobre las reseñas y consultas específicas.
 */
@RestController
@RequestMapping("/reviews")
@Tag(name = "Reseñas", description = "API para la gestión de reseñas y valoraciones de clínicas y tratamientos en Meditourism")
public class ReviewController {

    @Autowired
    IReviewService reviewService;

    /**
     * Obtiene una lista de todas las reseñas disponibles. Requiere autenticación con rol 'ADMIN'.
     * @return ResponseEntity con una lista de ReviewResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todas las reseñas",
            description = "Recupera una lista de todas las reseñas registradas en el sistema. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reseñas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
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
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /**
     * Obtiene una lista de reseñas asociadas a una clínica específica. Este endpoint es público.
     * @param id El ID de la clínica.
     * @return ResponseEntity con una lista de ReviewResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener reseñas por ID de clínica",
            description = "Recupera una lista de todas las reseñas de una clínica específica. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reseñas de la clínica recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Clínica no encontrada o sin reseñas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"No se encontraron reseñas para la clínica con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/review-clinic/{id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByClinicId(
            @Parameter(description = "ID de la clínica para filtrar las reseñas", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewsByClinicId(id));
    }

    /**
     * Obtiene una reseña por su ID. Requiere autenticación con rol 'ADMIN'.
     * @param id El ID de la reseña a buscar.
     * @return ResponseEntity con el ReviewResponseDTO de la reseña encontrada y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener reseña por ID",
            description = "Recupera los detalles de una reseña específica utilizando su ID. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Reseña no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewByReviewId(
            @Parameter(description = "ID de la reseña a recuperar", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    /**
     * Obtiene una lista de reseñas creadas por un usuario específico. Este endpoint es público.
     * @param id El ID del usuario.
     * @return ResponseEntity con una lista de ReviewResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener reseñas por ID de usuario",
            description = "Recupera una lista de todas las reseñas creadas por un usuario específico. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reseñas del usuario recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o sin reseñas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"No se encontraron reseñas para el usuario con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUserId(
            @Parameter(description = "ID del usuario para filtrar las reseñas", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewsByUserId(id));
    }

    /**
     * Publica una nueva reseña. Requiere autenticación de usuario.
     * @param dto El ReviewRequestDTO que contiene los datos de la nueva reseña.
     * @return ResponseEntity con el ReviewResponseDTO de la reseña guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear una nueva reseña",
            description = "Permite a un usuario autenticado crear una nueva reseña para una clínica. Requiere autenticación con token JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"rating\": \"La valoración no puede estar vacía\"}}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "404", description = "Clínica o usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"Clínica no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> postReview(
            @Parameter(description = "Datos de la reseña a guardar. Los campos 'clinicId', 'userId', 'rating' y 'comment' son obligatorios.", required = true,
                    schema = @Schema(implementation = ReviewRequestDTO.class))
            @RequestBody @Valid ReviewRequestDTO dto){
        ReviewResponseDTO savedReview = reviewService.saveReview(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedReview.getId())
                        .toUri())
                .body(savedReview);
    }

    /**
     * Actualiza parcialmente una reseña existente. Requiere autenticación de usuario.
     * @param id El ID de la reseña a actualizar.
     * @param dto El ReviewRequestDTO con los campos a actualizar.
     * @return ResponseEntity con el ReviewResponseDTO de la reseña actualizada y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar una reseña",
            description = "Permite a un usuario autenticado actualizar su propia reseña. Requiere autenticación con token JWT. Se pueden actualizar uno o más campos (ej. 'rating', 'comment').",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no es el propietario de la reseña o no tiene rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes permisos para modificar esta reseña.\" }"))),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Reseña no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> patchReview(
            @Parameter(description = "ID de la reseña a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de la reseña a actualizar. Se pueden enviar solo los campos que se desean modificar (ej. {\"comment\": \"Excelente experiencia!\"}).", required = true,
                    schema = @Schema(implementation = ReviewRequestDTO.class))
            @RequestBody ReviewRequestDTO dto){
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    /**
     * Elimina una reseña por su ID. Requiere autenticación de usuario (propietario) o rol 'ADMIN'.
     * @param id El ID de la reseña a eliminar.
     * @return ResponseEntity con el ReviewResponseDTO de la reseña eliminada y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar una reseña por ID",
            description = "Permite al propietario de la reseña o a un usuario con rol 'ADMIN' eliminar una reseña del sistema. Requiere autenticación con token JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña eliminada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no es el propietario de la reseña o no tiene rol 'ADMIN'.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes permisos para eliminar esta reseña.\" }"))),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Reseña no encontrada con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> deleteReview(
            @Parameter(description = "ID de la reseña a eliminar", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

    /**
     * Obtiene las tres reseñas más recientes. Este endpoint es público.
     * @return ResponseEntity con una lista de ReviewResponseDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener las 3 reseñas más recientes",
            description = "Recupera las tres reseñas más recientemente añadidas al sistema. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseñas recientes recuperadas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/latest")
    public ResponseEntity<List<ReviewResponseDTO>> getLatestThreeReviews() {
        return ResponseEntity.ok(reviewService.getLatestThreeReviews());
    }
}