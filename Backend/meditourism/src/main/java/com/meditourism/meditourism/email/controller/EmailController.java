package com.meditourism.meditourism.email.controller;

import com.meditourism.meditourism.email.dto.EmailRequest;
import com.meditourism.meditourism.email.service.IEmailService;
import com.meditourism.meditourism.jwt.IJwtService;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// Swagger/OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador para el envío de correos electrónicos.
 * Proporciona endpoints para enviar correos electrónicos genéricos y de bienvenida.
 */
@RestController
@RequestMapping("/email")
@Tag(name = "Email", description = "API para el envío de correos electrónicos")
public class EmailController {

    @Autowired
    private IEmailService emailService;

    /**
     * Envía un correo electrónico genérico.
     * @param request Objeto EmailRequest con los datos del correo (destinatario, asunto, cuerpo)
     * @return Mensaje de confirmación del envío
     */
    @Operation(summary = "Enviar correo electrónico",
            description = "Envía un correo electrónico genérico a la dirección especificada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Correo enviado exitosamente"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al enviar el correo",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public String sendEmail(
            @Parameter(description = "Datos del correo a enviar", required = true,
                    schema = @Schema(implementation = EmailRequest.class))
            @RequestBody EmailRequest request) {
        emailService.sendEmail(request.getRecipient(), request.getSubject(), request.getBody());
        return "Correo enviado exitosamente";
    }

    /**
     * Envía un correo electrónico de bienvenida.
     * @param request Objeto EmailRequest con los datos del correo (destinatario, asunto, cuerpo)
     * @return ResponseEntity con mensaje de confirmación del envío
     */
    @Operation(summary = "Enviar correo de bienvenida",
            description = "Envía un correo electrónico de bienvenida a la dirección especificada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de bienvenida enviado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Correo de bienvenida enviado exitosamente"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al enviar el correo",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/send-welcome-email")
    public ResponseEntity<String> sendWelcomeEmail(
            @Parameter(description = "Datos del correo de bienvenida a enviar", required = true,
                    schema = @Schema(implementation = EmailRequest.class))
            @RequestBody EmailRequest request) {
        emailService.sendEmail(request.getRecipient(), request.getSubject(), request.getBody());
        return ResponseEntity.ok("Correo de bienvenida enviado exitosamente");
    }
}