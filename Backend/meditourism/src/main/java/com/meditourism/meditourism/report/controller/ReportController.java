package com.meditourism.meditourism.report.controller;

import com.meditourism.meditourism.report.dto.ReportRequestDTO;
import com.meditourism.meditourism.report.dto.ReportResponseDTO;
import com.meditourism.meditourism.report.service.IReportService;
import jakarta.validation.Valid;
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
 * Controlador para la gestión de reportes en el sistema Meditourism.
 * Proporciona operaciones CRUD para reportes y funcionalidades relacionadas.
 */
@RestController
@RequestMapping("/reports")
@Tag(name = "Reportes", description = "API para la gestión de reportes en Meditourism")
public class ReportController {

    @Autowired
    private IReportService reportService;

    /**
     * Obtiene todos los reportes del sistema.
     * @return Lista de todos los reportes con estado HTTP 200 OK
     */
    @Operation(summary = "Obtener todos los reportes",
            description = "Recupera una lista de todos los reportes disponibles en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reportes recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class, type = "array"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-05-30T10:00:00.000Z\", \"status\": 500, \"error\": \"Error Interno del Servidor\", \"message\": \"Ocurrió un error al procesar la solicitud\"}")))
    })
    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    /**
     * Obtiene un reporte específico por su ID.
     * @param id ID del reporte a recuperar
     * @return El reporte solicitado con estado HTTP 200 OK
     */
    @Operation(summary = "Obtener reporte por ID",
            description = "Recupera un reporte específico utilizando su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte recuperado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-05-30T10:00:00.000Z\", \"status\": 404, \"error\": \"No Encontrado\", \"message\": \"No se encontró el reporte con id 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(
            @Parameter(description = "ID del reporte a recuperar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    /**
     * Crea un nuevo reporte en el sistema.
     * @param dto Datos del reporte a crear
     * @return El reporte creado con estado HTTP 201 Created
     */
    @Operation(summary = "Crear un nuevo reporte",
            description = "Crea un nuevo reporte con los datos proporcionados",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-05-30T10:00:00.000Z\", \"status\": 400, \"error\": \"Solicitud Incorrecta\", \"message\": \"Validación fallida para el campo 'título': no debe estar vacío\"}"))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Autenticación requerida",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos insuficientes",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(
            @Parameter(description = "Datos del reporte a crear", required = true,
                    schema = @Schema(implementation = ReportRequestDTO.class))
            @RequestBody @Valid ReportRequestDTO dto) {
        ReportResponseDTO savedReport = reportService.saveReport(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedReport.getId())
                        .toUri())
                .body(savedReport);
    }

    /**
     * Actualiza un reporte existente.
     * @param id ID del reporte a actualizar
     * @param dto Datos actualizados del reporte
     * @return El reporte actualizado con estado HTTP 200 OK
     */
    @Operation(summary = "Actualizar un reporte",
            description = "Actualiza un reporte existente con los datos proporcionados",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "No autorizado - Autenticación requerida",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos insuficientes",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> updateReport(
            @Parameter(description = "ID del reporte a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del reporte", required = true,
                    schema = @Schema(implementation = ReportRequestDTO.class))
            @RequestBody ReportRequestDTO dto) {
        return ResponseEntity.ok(reportService.updateReport(id, dto));
    }

    /**
     * Elimina un reporte del sistema.
     * @param id ID del reporte a eliminar
     * @return El reporte eliminado con estado HTTP 200 OK
     */
    @Operation(summary = "Eliminar un reporte",
            description = "Elimina un reporte específico del sistema",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Autenticación requerida",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos insuficientes",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> deleteReport(
            @Parameter(description = "ID del reporte a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(reportService.deleteReport(id));
    }
}
