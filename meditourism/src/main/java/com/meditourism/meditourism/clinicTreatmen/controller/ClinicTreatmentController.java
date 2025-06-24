package com.meditourism.meditourism.clinicTreatmen.controller;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentResponseDTO;
import com.meditourism.meditourism.clinicTreatmen.service.IClinicTreatmentService;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
 * **Controlador REST para la gestión de la relación entre clínicas y tratamientos en Meditourism.**
 *
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre las asociaciones entre clínicas y los tratamientos que ofrecen,
 * así como consultar clínicas por tratamiento y tratamientos por clínica.
 */
@RestController
@RequestMapping("/clinics-treatments")
@Tag(name = "Clínicas y Tratamientos", description = "API para gestionar la asociación entre clínicas y tratamientos en Meditourism")
public class ClinicTreatmentController {

    @Autowired
    private IClinicTreatmentService clinicTreatmentService;

    /**
     * **Obtiene una lista de todas las asociaciones existentes entre clínicas y tratamientos.**
     *
     * Este endpoint recupera todas las relaciones donde se especifica qué tratamiento ofrece cada clínica, incluyendo detalles de la clínica, el tratamiento y el precio asociado.
     * Es accesible públicamente.
     *
     * @return {@link ResponseEntity} con una lista de {@link ClinicTreatmentResponseDTO} y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener todas las asociaciones de clínicas y tratamientos",
            description = "Recupera una lista de todas las relaciones existentes entre clínicas y los tratamientos que ofrecen. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de asociaciones recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicTreatmentResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar las asociaciones",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping
    public ResponseEntity<List<ClinicTreatmentResponseDTO>> getAllClinicTreatments(){
        return ResponseEntity.ok(clinicTreatmentService.getAllClinicTreatments());
    }

    /**
     * **Obtiene una asociación específica entre una clínica y un tratamiento utilizando sus IDs compuestos.**
     *
     * Permite buscar una relación particular entre una clínica y un tratamiento proporcionando el ID de la clínica y el ID del tratamiento.
     * Este endpoint es accesible públicamente.
     *
     * @param clinicId El **ID de la clínica** a buscar en la asociación.
     * @param treatmentId El **ID del tratamiento** a buscar en la asociación.
     * @return {@link ResponseEntity} con el {@link ClinicTreatmentResponseDTO} de la asociación encontrada y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener asociación de clínica y tratamiento por IDs",
            description = "Recupera una asociación específica entre una clínica y un tratamiento proporcionando los IDs de ambos. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asociación encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicTreatmentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "La asociación no fue encontrada con los IDs de clínica y tratamiento proporcionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Asociación no encontrada para Clinic ID: 1, Treatment ID: 5\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar la asociación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/clinic-treatment")
    public ResponseEntity<ClinicTreatmentResponseDTO> getClinicTreatmentByIds(
            @Parameter(description = "ID de la clínica", required = true, example = "1")
            @RequestParam Long clinicId,
            @Parameter(description = "ID del tratamiento", required = true, example = "5")
            @RequestParam Long treatmentId
    ) {
        ClinicTreatmentDTO dto = new ClinicTreatmentDTO();
        dto.setClinicId(clinicId);
        dto.setTreatmentId(treatmentId);
        return ResponseEntity.ok(clinicTreatmentService.getClinicTreatmentById(dto));
    }

    /**
     * **Obtiene una lista de clínicas que ofrecen un tratamiento específico.**
     *
     * Permite encontrar todas las clínicas que tienen un tratamiento en particular, utilizando el ID de ese tratamiento.
     * Este endpoint es accesible públicamente.
     *
     * @param id El **ID del tratamiento** para el cual se desean recuperar las clínicas.
     * @return {@link ResponseEntity} con una lista de {@link ClinicDTO} (las clínicas encontradas) y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener clínicas por ID de tratamiento",
            description = "Recupera una lista de todas las clínicas que ofrecen un tratamiento específico utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clínicas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron clínicas que ofrezcan el tratamiento con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"No se encontraron clínicas para el tratamiento con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar las clínicas por tratamiento",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/clinics/{id}")
    public ResponseEntity<List<ClinicDTO>> getClinicsByTreatmentId(
            @Parameter(description = "ID del tratamiento para el cual se desean recuperar las clínicas", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(clinicTreatmentService.getClinicsByTreatmentId(id));
    }

    /**
     * **Obtiene una lista de tratamientos ofrecidos por una clínica específica.**
     *
     * Permite buscar todos los tratamientos que una clínica en particular ofrece, utilizando el ID de esa clínica.
     * Este endpoint es accesible públicamente.
     *
     * @param id El **ID de la clínica** para la cual se desean recuperar los tratamientos.
     * @return {@link ResponseEntity} con una lista de {@link TreatmentDTO} (los tratamientos encontrados) y estado HTTP 200 OK.
     */
    @Operation(summary = "Obtener tratamientos por ID de clínica",
            description = "Recupera una lista de todos los tratamientos ofrecidos por una clínica específica utilizando su ID. Este endpoint es accesible públicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tratamientos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron tratamientos para la clínica con el ID proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"No se encontraron tratamientos para la clínica con ID: 99\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar los tratamientos por clínica",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/treatments/{id}")
    public ResponseEntity<List<TreatmentDTO>> getTreatmentsByClinicId(
            @Parameter(description = "ID de la clínica para la cual se desean recuperar los tratamientos", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(clinicTreatmentService.getTreatmentsByClinicId(id));
    }

    /**
     * **Crea una nueva asociación entre una clínica y un tratamiento.**
     *
     * Permite a un usuario con rol **'ADMIN'** asociar un tratamiento específico a una clínica existente.
     * Este proceso implica definir qué tratamiento se ofrece en qué clínica y el precio de dicha oferta.
     * Requiere autenticación mediante un token JWT y que el usuario posea el rol 'ADMIN'.
     *
     * @param clinicTreatment El {@link ClinicTreatmentDTO} que contiene el **ID de la clínica**, el **ID del tratamiento** a asociar y el **precio** del tratamiento en esa clínica.
     * @return {@link ResponseEntity} con el {@link ClinicTreatmentResponseDTO} de la asociación guardada y estado HTTP 200 OK.
     */
    @Operation(summary = "Crear una nueva asociación de clínica y tratamiento",
            description = "Permite a un usuario con rol 'ADMIN' asociar un tratamiento a una clínica, especificando el precio. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asociación creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicTreatmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Puede deberse a IDs de clínica o tratamiento faltantes, incorrectos o formatos inválidos. También si el precio es inválido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Los IDs de clínica y tratamiento son obligatorios.\" }"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido para acceder a este recurso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario autenticado no tiene el rol 'ADMIN' para realizar esta acción.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Clínica o Tratamiento no encontrado. Si alguno de los IDs proporcionados no corresponde a una entidad existente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Clinica no encontrada con ID: 100\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. La asociación entre la clínica y el tratamiento ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Conflicto\", \"message\": \"La clínica ya ofrece este tratamiento.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar crear la asociación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ClinicTreatmentResponseDTO> saveClinicTreatment(
            @Parameter(description = "**Datos de la asociación clínica-tratamiento a guardar.** Se requieren el `clinicId`, `treatmentId` y opcionalmente el `price`.", required = true,
                    schema = @Schema(implementation = ClinicTreatmentDTO.class))
            @RequestBody ClinicTreatmentDTO clinicTreatment ){
        return ResponseEntity.ok(clinicTreatmentService.saveClinicTreatment(clinicTreatment));
    }

    /**
     * **Actualiza el precio de una asociación existente entre una clínica y un tratamiento.**
     *
     * Este endpoint permite a un usuario con rol **'ADMIN'** modificar el precio de un tratamiento ofrecido por una clínica específica.
     * La asociación se identifica mediante los IDs de la clínica y el tratamiento.
     * Requiere autenticación mediante un token JWT y que el usuario posea el rol 'ADMIN'.
     *
     * @param clinicId El **ID de la clínica** de la asociación a actualizar.
     * @param treatmentId El **ID del tratamiento** de la asociación a actualizar.
     * @param dto El {@link ClinicTreatmentDTO} que contiene el **nuevo precio** a establecer para la asociación. Otros campos en el DTO serán ignorados.
     * @return {@link ResponseEntity} con el {@link ClinicTreatmentResponseDTO} de la asociación actualizada y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualizar el precio de una asociación de clínica y tratamiento",
            description = "Permite a un usuario con rol 'ADMIN' actualizar el precio de un tratamiento ofrecido por una clínica existente. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precio de la asociación actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicTreatmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Puede deberse a IDs de clínica o tratamiento faltantes o incorrectos, o un precio inválido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Los IDs de clínica y tratamiento son obligatorios.\" }"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido para acceder a este recurso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario autenticado no tiene el rol 'ADMIN' para realizar esta acción.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Asociación no encontrada con los IDs de clínica y tratamiento proporcionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Asociación no encontrada para Clinic ID: 1, Treatment ID: 5\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar actualizar la asociación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ClinicTreatmentResponseDTO> updateClinicTreatment(
            @Parameter(description = "ID de la clínica de la asociación a actualizar", required = true, example = "1")
            @RequestParam Long clinicId,
            @Parameter(description = "ID del tratamiento de la asociación a actualizar", required = true, example = "5")
            @RequestParam Long treatmentId,
            @Parameter(description = "**Datos de la asociación clínica-tratamiento para la actualización.** Solo se espera el campo `price` para modificar el precio de la asociación. Otros campos serán ignorados.",
                    schema = @Schema(implementation = ClinicTreatmentDTO.class))
            @RequestBody ClinicTreatmentDTO dto){
        return ResponseEntity.ok(clinicTreatmentService.updateClinicTreatment(clinicId, treatmentId, dto));
    }


    /**
     * **Elimina una asociación existente entre una clínica y un tratamiento.**
     *
     * Permite a un usuario con rol **'ADMIN'** remover la relación que indica que una clínica ofrece un tratamiento específico.
     * La asociación se identifica mediante los IDs de la clínica y el tratamiento.
     * Requiere autenticación mediante un token JWT y que el usuario posea el rol 'ADMIN'.
     *
     * @param clinicId El **ID de la clínica** de la asociación a eliminar.
     * @param treatmentId El **ID del tratamiento** de la asociación a eliminar.
     * @return {@link ResponseEntity} con el {@link ClinicTreatmentResponseDTO} de la asociación eliminada y estado HTTP 200 OK.
     */
    @Operation(summary = "Eliminar una asociación de clínica y tratamiento",
            description = "Permite a un usuario con rol 'ADMIN' eliminar una asociación existente entre una clínica y un tratamiento. Requiere autenticación con token JWT y rol de ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asociación eliminada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicTreatmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Puede deberse a IDs de clínica o tratamiento faltantes o incorrectos.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Los IDs de clínica y tratamiento son obligatorios.\" }"))),
            @ApiResponse(responseCode = "401", description = "No autenticado. Se requiere un token JWT válido para acceder a este recurso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario autenticado no tiene el rol 'ADMIN' para realizar esta acción.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 403, \"error\": \"Acceso Denegado\", \"message\": \"No tienes los permisos necesarios para realizar esta acción. Se requiere el rol de 'ADMIN'.\"}"))),
            @ApiResponse(responseCode = "404", description = "Asociación no encontrada con los IDs de clínica y tratamiento proporcionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"Recurso no encontrado\", \"message\": \"Asociación no encontrada para Clinic ID: 1, Treatment ID: 5\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar eliminar la asociación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<ClinicTreatmentResponseDTO> deleteClinicTreatment(
            @Parameter(description = "ID de la clínica de la asociación a eliminar", required = true, example = "1")
            @RequestParam Long clinicId,
            @Parameter(description = "ID del tratamiento de la asociación a eliminar", required = true, example = "5")
            @RequestParam Long treatmentId){
        return ResponseEntity.ok(clinicTreatmentService.deleteClinicTreatment(clinicId, treatmentId));
    }
}
