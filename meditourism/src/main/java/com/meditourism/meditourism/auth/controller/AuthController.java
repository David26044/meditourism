package com.meditourism.meditourism.auth.controller;

import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.ChangePasswordDTO;
import com.meditourism.meditourism.auth.service.IAuthService;
import com.meditourism.meditourism.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
 * Controlador REST para la autenticación y gestión de usuarios.
 * Proporciona endpoints para el registro, inicio de sesión, y operaciones de recuperación/verificación de contraseña.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "API para la gestión de autenticación y usuarios en Meditourism")
public class AuthController {

    @Autowired
    private IAuthService authService;

    /**
     * Realiza el inicio de sesión de un usuario.
     * @param request Objeto AuthRequest que contiene las credenciales del usuario (email y contraseña).
     * @return ResponseEntity con un AuthResponse que incluye el token JWT y los datos del usuario.
     */
    @Operation(summary = "Iniciar sesión de usuario",
            description = "Permite a un usuario existente iniciar sesión y obtener un token de autenticación JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Correo o contraseña incorrectos\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Credenciales del usuario (email y password)", required = true,
                    schema = @Schema(implementation = AuthRequest.class))
            @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param request Objeto UserDTO que contiene los datos del nuevo usuario.
     * @return ResponseEntity con un AuthResponse que incluye el token JWT y los datos del usuario registrado.
     */
    @Operation(summary = "Registrar un nuevo usuario",
            description = "Crea una nueva cuenta de usuario en el sistema. Se requiere que el email sea único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro exitoso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Validación fallida\", \"message\": \"Uno o más campos tienen errores\", \"errors\": {\"email\": \"El email no puede estar vacío\"}}"))),
            @ApiResponse(responseCode = "409", description = "Conflicto. Un usuario con el mismo email ya existe.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Conflicto\", \"message\": \"Ya existe un usuario con este email.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Datos del nuevo usuario a registrar. Los campos 'email', 'password', 'firstName', 'lastName' son obligatorios.", required = true,
                    schema = @Schema(implementation = UserDTO.class))
            @RequestBody @Valid UserDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Envía un correo electrónico para iniciar el proceso de cambio de contraseña.
     * @param email El correo electrónico del usuario que desea cambiar su contraseña.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @Operation(summary = "Solicitar cambio de contraseña",
            description = "Envía un correo electrónico al usuario con un enlace para restablecer su contraseña. Este endpoint es público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de recuperación enviado con éxito",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Correo de recuperación enviado con éxito."))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el email proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"Usuario no encontrado con email: test@example.com\"}"))),
            @ApiResponse(responseCode = "500", description = "Error al enviar el correo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"No se pudo enviar el correo de recuperación.\" }")))
    })
    @PostMapping("/send-change-password")
    public ResponseEntity<String> sendChangePassword(
            @Parameter(description = "Correo electrónico del usuario para enviar el enlace de cambio de contraseña", required = true, example = "usuario@example.com")
            @RequestParam("email") String email) {
        authService.sendEmailChangePassword(email);
        return ResponseEntity.ok("Correo de recuperación enviado con éxito.");
    }

    /**
     * Cambia la contraseña de un usuario utilizando un token de recuperación.
     * @param token El token de recuperación recibido por correo.
     * @param dto El objeto ChangePasswordDTO que contiene la nueva contraseña.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @Operation(summary = "Cambiar contraseña",
            description = "Permite a un usuario cambiar su contraseña utilizando un token de recuperación válido recibido por correo. Este endpoint es público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña cambiada con éxito",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Contraseña cambiada con éxito."))),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado, o contraseñas no coinciden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Token inválido o expirado.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Parameter(description = "Token de recuperación de contraseña enviado al correo del usuario", required = true)
            @RequestParam("token") String token,
            @Parameter(description = "Nuevas contraseñas (newPassword y confirmPassword)", required = true,
                    schema = @Schema(implementation = ChangePasswordDTO.class))
            @RequestBody ChangePasswordDTO dto) {
        authService.changePassword(token, dto);
        return ResponseEntity.ok("Contraseña cambiada con éxito.");
    }

    /**
     * Envía un correo electrónico para verificar la cuenta de usuario.
     * @param email El correo electrónico del usuario a verificar.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @Operation(summary = "Enviar correo de verificación",
            description = "Envía un correo electrónico al usuario con un enlace para verificar su cuenta. Este endpoint es público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de verificación enviado con éxito",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Correo de verificación enviado a usuario@example.com"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el email proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"Usuario no encontrado con email: test@example.com\"}"))),
            @ApiResponse(responseCode = "409", description = "El correo ya ha sido verificado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 409, \"error\": \"Conflicto\", \"message\": \"El correo ya ha sido verificado.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error al enviar el correo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"No se pudo enviar el correo de verificación.\" }")))
    })
    @PostMapping("/send-verify-email")
    public ResponseEntity<String> sendVerifyEmail(
            @Parameter(description = "Correo electrónico del usuario para enviar el enlace de verificación", required = true, example = "usuario@example.com")
            @RequestParam("email") String email) {
        authService.sendEmailVerification(email);
        return ResponseEntity.ok("Correo de verificación enviado a " + email);
    }

    /**
     * Confirma la verificación de un correo electrónico utilizando un token.
     * @param token El token de verificación recibido por correo.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @Operation(summary = "Confirmar verificación de correo electrónico",
            description = "Verifica la cuenta de un usuario utilizando un token válido recibido por correo. Este endpoint es público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo verificado correctamente",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Correo verificado correctamente."))),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Token de verificación inválido o expirado.\" }"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado asociado al token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 404, \"error\": \"No encontrado\", \"message\": \"Usuario no encontrado para el token proporcionado.\" }"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2025-05-29T12:30:00.000000\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Mensaje de error genérico\"}")))
    })
    @GetMapping("/verify-email")
    public ResponseEntity<String> confirmEmail(
            @Parameter(description = "Token de verificación de correo electrónico", required = true)
            @RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Correo verificado correctamente.");
    }
}


