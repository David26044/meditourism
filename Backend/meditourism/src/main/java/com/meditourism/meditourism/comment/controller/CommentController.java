package com.meditourism.meditourism.comment.controller;

import com.meditourism.meditourism.comment.dto.CommentDTO;
import com.meditourism.meditourism.comment.service.CommentService;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST para la gestión de comentarios.
 * Proporciona endpoints para operaciones CRUD y consultas sobre comentarios.
 */
@RestController
@RequestMapping("/comments")
@Tag(name = "Comentarios", description = "API para la gestión de comentarios en Meditourism")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Obtiene una lista de todos los comentarios disponibles.
     * @return ResponseEntity con una lista de CommentDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todos los comentarios",
            description = "Recupera una lista de todos los comentarios registrados en el sistema. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    /**
     * Obtiene comentarios filtrados por el ID de una reseña (review).
     * @param id El ID de la reseña.
     * @return ResponseEntity con una lista de CommentDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener comentarios por ID de reseña",
            description = "Recupera una lista de comentarios asociados a una reseña específica por su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron comentarios para la reseña con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"No se encontraron comentarios para la reseña con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/review/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsByReviewId(
            @Parameter(description = "ID de la reseña para la cual se desean recuperar los comentarios", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByReviewId(id));
    }

    /**
     * Obtiene las respuestas a un comentario específico por su ID.
     * @param id El ID del comentario padre.
     * @return ResponseEntity con una lista de CommentDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener respuestas a un comentario",
            description = "Recupera una lista de respuestas (comentarios hijos) asociadas a un comentario padre por su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de respuestas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron respuestas para el comentario con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"No se encontraron respuestas para el comentario con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/reply/{id}")
    public ResponseEntity<List<CommentDTO>> getRepliesByCommentId(
            @Parameter(description = "ID del comentario padre para el cual se desean recuperar las respuestas", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getRepliesByCommentId(id));
    }

    /**
     * Obtiene un comentario por su ID.
     * @param id El ID del comentario a buscar.
     * @return ResponseEntity con el CommentDTO del comentario encontrado y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener comentario por ID",
            description = "Recupera los detalles de un comentario específico utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentario encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Comentario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(
            @Parameter(description = "ID del comentario a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    /**
     * Obtiene comentarios filtrados por el ID de un usuario.
     * @param id El ID del usuario.
     * @return ResponseEntity con una lista de CommentDTO y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener comentarios por ID de usuario",
            description = "Recupera una lista de comentarios realizados por un usuario específico por su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron comentarios para el usuario con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"No se encontraron comentarios para el usuario con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(
            @Parameter(description = "ID del usuario para el cual se desean recuperar los comentarios", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(id));
    }

    /**
     * Crea un nuevo comentario en el sistema. Requiere autenticación.
     * @param dto El CommentDTO que contiene los datos del nuevo comentario.
     * @return ResponseEntity con el CommentDTO del comentario guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear un nuevo comentario",
            description = "Permite a un usuario autenticado registrar un nuevo comentario en el sistema. Requiere autenticación con token JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping
    public ResponseEntity<CommentDTO> saveComment(
            @Parameter(description = "Datos del comentario a guardar. Incluye 'content', 'userId', 'reviewId' (opcional), 'parentId' (opcional).", required = true,
                    schema = @Schema(implementation = CommentDTO.class))
            @RequestBody CommentDTO dto) {
        CommentDTO savedComment = commentService.saveComment(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedComment.getId())
                        .toUri())
                .body(savedComment);
    }

    /**
     * Actualiza un comentario existente por su ID. Requiere autenticación.
     * @param id El ID del comentario a actualizar.
     * @param dto El CommentDTO con los datos actualizados del comentario.
     * @return ResponseEntity con el CommentDTO del comentario actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar un comentario",
            description = "Permite a un usuario autenticado actualizar los detalles de un comentario existente. Requiere autenticación con token JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. formato de datos incorrecto)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Mensaje de error de formato\"}"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene permisos para actualizar este comentario.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción.\" }"))),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Comentario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @Parameter(description = "ID del comentario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del comentario a actualizar. Se pueden enviar solo los campos que se desean modificar.", required = true,
                    schema = @Schema(implementation = CommentDTO.class))
            @RequestBody CommentDTO dto) {
        return ResponseEntity.ok(commentService.updateComment(id, dto));
    }

    /**
     * Elimina un comentario por su ID. Requiere autenticación.
     * @param id El ID del comentario a eliminar.
     * @return ResponseEntity con el CommentDTO del comentario eliminado y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar un comentario por ID",
            description = "Permite a un usuario autenticado eliminar un comentario del sistema utilizando su ID. Requiere autenticación con token JWT. Un usuario solo puede eliminar sus propios comentarios, o un ADMIN puede eliminar cualquier comentario.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentario eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene permisos para eliminar este comentario.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción.\" }"))),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Comentario no encontrado con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDTO> deleteComment(
            @Parameter(description = "ID del comentario a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}