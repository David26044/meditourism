package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.jwt.IJwtService;
import com.meditourism.meditourism.role.service.IRoleService;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isOwner(Long ownerId) {
        UserEntity user = getAuthenticatedUser();
        return user.getId().equals(ownerId);
    }

    public boolean isOwnerOrAdmin(Long ownerId) {
        return isOwner(ownerId) || isAdmin();
    }
}
