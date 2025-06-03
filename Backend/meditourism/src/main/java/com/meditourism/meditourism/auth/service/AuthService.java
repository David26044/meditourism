package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.auth.dto.ChangePasswordDTO;
import com.meditourism.meditourism.email.service.IEmailService;
import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.jwt.IJwtService;
import com.meditourism.meditourism.role.service.IRoleService;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar la autenticación, registro y gestión de usuarios.
 */
@Service
public class AuthService implements IAuthService {

    @Autowired
    IRoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    IEmailService emailService;

    @Value("${app.frontend.reset-password.url}")
    private String frontendResetPasswordUrl;
    @Value("${app.email.verification-url}")
    private String verificationUrlBase;

    /**
     * Autentica a un usuario y genera un token JWT.
     *
     * @param request DTO con las credenciales de autenticación (email y contraseña)
     * @return Respuesta de autenticación con el token JWT
     * @throws ResourceNotFoundException Si no existe un usuario con el email proporcionado
     */
    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No existe usuario registrado con el correo: " + request.getEmail()));
        String token = jwtService.getToken(user);
        return new AuthResponse(token);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request DTO con los datos del nuevo usuario
     * @return Respuesta de autenticación con el token JWT
     * @throws ResourceAlreadyExistsException Si ya existe un usuario con el email proporcionado
     */
    @Override
    public AuthResponse register(UserDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Ya hay un usuario registrado con el correo: " + request.getEmail());
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRoleEntity(roleService.getRoleById(2L));
        user.setVerified(false);
        userRepository.save(user);

        // Enviar correo de bienvenida automáticamente
        try {
            sendWelcomeEmail(user.getEmail(), user.getName());
        } catch (Exception e) {
            // Log del error pero no fallar el registro si el correo no se puede enviar
            System.err.println("Error enviando correo de bienvenida a " + user.getEmail() + ": " + e.getMessage());
        }

        AuthResponse response = new AuthResponse();
        response.setToken(jwtService.getToken(user));
        return response;
    }

    /**
     * Envía un correo electrónico con un enlace para restablecer la contraseña.
     *
     * @param email Email del usuario que solicita el restablecimiento
     * @throws ResourceNotFoundException Si no existe un usuario con el email proporcionado
     */
    @Override
    public void sendEmailChangePassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        String token = jwtService.generateVerificationToken(email);
        String resetUrl = frontendResetPasswordUrl + "?token=" + token;

        String subject = "Restablecimiento de contraseña - MediTourism";
        String body = "Hola " + userEntity.getName() + ",\n\n" +
                "Has solicitado restablecer tu contraseña en MediTourism.\n\n" +
                "Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + resetUrl + "\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este mensaje.\n\n" +
                "Este enlace expirará en 24 horas por seguridad.\n\n" +
                "Saludos,\nEquipo MediTourism";

        emailService.sendEmail(email, subject, body);
    }

    /**
     * Cambia la contraseña de un usuario usando un token de verificación.
     *
     * @param token Token JWT de verificación
     * @param dto DTO con la nueva contraseña
     * @throws ResourceNotFoundException Si no existe un usuario con el email asociado al token
     */
    @Override
    public void changePassword(String token, ChangePasswordDTO dto) {
        String email = jwtService.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        userEntity.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(userEntity);
    }

    /**
     * Envía un correo electrónico de verificación al usuario recién registrado.
     *
     * @param email Email del usuario a verificar
     * @throws ResourceNotFoundException Si no existe un usuario con el email proporcionado
     */
    @Override
    public void sendEmailVerification(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        String token = jwtService.generateVerificationToken(email);
        String verificationLink = verificationUrlBase + "?token=" + token;

        String subject = "Verifica tu correo electrónico - MediTourism";
        String body = "¡Bienvenido a MediTourism, " + userEntity.getName() + "!\n\n" +
                "Gracias por registrarte en nuestra plataforma de turismo médico.\n\n" +
                "Para completar tu registro, por favor verifica tu correo electrónico haciendo clic en el siguiente enlace:\n" +
                verificationLink + "\n\n" +
                "Una vez verificado tu correo, podrás acceder a todos nuestros servicios.\n\n" +
                "Si no te registraste en MediTourism, puedes ignorar este mensaje.\n\n" +
                "¡Esperamos ayudarte a encontrar los mejores tratamientos médicos!\n\n" +
                "Saludos,\nEquipo MediTourism";

        emailService.sendEmail(email, subject, body);
    }

    /**
     * Verifica el email de un usuario usando un token JWT.
     *
     * @param token Token JWT de verificación
     * @throws ResourceNotFoundException Si no existe un usuario con el email asociado al token
     */
    @Override
    public void verifyEmail(String token) {
        String email = jwtService.getUsernameFromToken(token);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        user.setVerified(true);
        userRepository.save(user);
    }

    /**
     * Obtiene el usuario autenticado actualmente.
     *
     * @return Entidad del usuario autenticado
     */
    @Override
    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

    /**
     * Verifica si el usuario autenticado es administrador.
     *
     * @return true si el usuario tiene rol de administrador, false en caso contrario
     */
    @Override
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Verifica si el usuario autenticado es el propietario del recurso.
     *
     * @param ownerId ID del propietario del recurso
     * @return true si el usuario autenticado es el propietario, false en caso contrario
     */
    @Override
    public boolean isOwner(Long ownerId) {
        UserEntity user = getAuthenticatedUser();
        return user.getId().equals(ownerId);
    }

    /**
     * Verifica si el usuario autenticado es administrador o propietario del recurso.
     *
     * @param ownerId ID del propietario del recurso
     * @return true si el usuario es administrador o propietario, false en caso contrario
     */
    public boolean isOwnerOrAdmin(Long ownerId) {
        return isOwner(ownerId) || isAdmin();
    }

    /**
     * Envía un correo electrónico de bienvenida al usuario recién registrado.
     *
     * @param email Email del usuario
     * @param name Nombre del usuario
     */
    private void sendWelcomeEmail(String email, String name) {
        String subject = "¡Bienvenido a MediTourism!";
        String body = "¡Hola " + name + "!\n\n" +
                "¡Bienvenido a MediTourism! 🎉\n\n" +
                "Tu cuenta ha sido creada exitosamente. Nos complace tenerte como parte de nuestra comunidad dedicada al turismo médico.\n\n" +
                "Con MediTourism podrás:\n" +
                "• Explorar tratamientos médicos de alta calidad\n" +
                "• Consultar con especialistas certificados\n" +
                "• Acceder a servicios médicos a precios competitivos\n" +
                "• Obtener información sobre destinos médicos confiables\n\n" +
                "Para comenzar a usar todos nuestros servicios, te recomendamos verificar tu correo electrónico.\n\n" +
                "Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.\n\n" +
                "¡Gracias por elegir MediTourism para tu cuidado médico!\n\n" +
                "Saludos cordiales,\n" +
                "El equipo de MediTourism\n\n" +
                "---\n" +
                "Este es un correo automático, por favor no respondas a esta dirección.";

        emailService.sendEmail(email, subject, body);
    }
}
