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

    @Value("${app.email.reset-password-url}")
    private String resetPasswordUrlBase;
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
        String resetUrl = resetPasswordUrlBase + "?token=" + token;

        String subject = "Restablecimiento de contraseña";
        String body = "Hola " + userEntity.getName() + ",\n\n" +
                "Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + resetUrl;

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
        String verificationLink = verificationUrlBase +  "?token=" + token;
        String subject = "Verifica tu correo electrónico";
        String body = "Bienvenido a MediTourism. Haz clic en el siguiente enlace para verificar tu correo:\n\n" + verificationLink;
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
