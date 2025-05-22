package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthRequest;

public interface IAuthenticationService {
    /**
     * Retorna:
     * 0 -> Email no encontrado
     * 1 -> Clave incorrecta
     * 2 -> Login exitoso
     */
    int authenticate(AuthRequest request);
}

