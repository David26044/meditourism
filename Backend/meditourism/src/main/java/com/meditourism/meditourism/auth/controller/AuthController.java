package com.meditourism.meditourism.auth.controller;

import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.service.IAuthService;
import com.meditourism.meditourism.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserDTO request){
        return ResponseEntity.ok(authService.register(request));
    }

}



