package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.jwt.IJwtService;
import com.meditourism.meditourism.role.service.IRoleService;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{


    @Autowired
    IRoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IJwtService jwtService;


    @Override
    public AuthResponse login(AuthRequest request){
        return new AuthResponse();
    }

    @Override
    public AuthResponse register(UserDTO request){
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setRoleEntity(roleService.getRoleById(request.getRoleId()));
        user.setVerified(false);
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setToken(jwtService.getToken(user));
        return response;
    }

}
