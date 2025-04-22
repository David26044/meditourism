package com.meditourism.meditourism.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    // ✅ Clave secreta segura (256 bits - 32 caracteres)
    private static final String SECRET = "xU2jR7qZ8vGfT5kL0pWmNsEyAbCqHdXi"; // Cambia esta por una generada segura para tu entorno

    // 🔐 Convertimos el string a una clave válida para HS256
    private static final SecretKey SECRET_KEY = new SecretKeySpec(
            SECRET.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName()
    );

    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // Fecha actual
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
