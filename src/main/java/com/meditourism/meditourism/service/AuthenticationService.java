package com.meditourism.meditourism.service;

import com.meditourism.meditourism.dto.AuthenticationRequest;

public interface AuthenticationService {
    /**
     * Retorna:
     * 0 -> Email no encontrado
     * 1 -> Clave incorrecta
     * 2 -> Login exitoso
     */
    int authenticate(AuthenticationRequest request);
}

