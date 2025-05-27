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

@Service
public class AuthService implements IAuthService{


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


    @Override
    public AuthResponse login(AuthRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No existe usuario registrado con el correo: " + request.getEmail()));
        String token = jwtService.getToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse register(UserDTO request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Ya hay un usuario registrado con el correo:  " + request.getEmail());
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRoleEntity(roleService.getRoleById(2L));
        user.setVerified(false);
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setToken(jwtService.getToken(user));
        return response;
    }

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

    @Override
    public void changePassword(String token, ChangePasswordDTO dto) {
        String email = jwtService.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        userEntity.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void sendEmailVerification(String email){
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        String token = jwtService.generateVerificationToken(email);
        String verificationLink = "http://localhost:10090/verify-email.html?token=" + token;
        
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

    @Override
    public void verifyEmail(String token){
        // 1. Extraer el email del token
        String email = jwtService.getUsernameFromToken(token);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

    @Override
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public boolean isOwner(Long ownerId) {
        UserEntity user = getAuthenticatedUser();
        return user.getId().equals(ownerId);
    }

    public boolean isOwnerOrAdmin(Long ownerId) {
        return isOwner(ownerId) || isAdmin();
    }
}
