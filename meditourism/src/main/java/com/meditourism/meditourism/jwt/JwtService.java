package com.meditourism.meditourism.jwt;

import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Servicio para la generación, validación y procesamiento de tokens JWT.
 * Proporciona funcionalidades para:
 * - Generación de tokens de autenticación
 * - Generación de tokens de verificación de email
 * - Generación de tokens para restablecimiento de contraseña
 * - Validación de tokens
 * - Extracción de información de tokens
 */
@Service
public class JwtService implements IJwtService {

    @Autowired
    private IUserService userService;

    // Clave secreta para firmar los tokens (256 bits - 32 caracteres)
    private static final String SECRET = "4f8c1e2b3d5f6a7e8b9c0d1e2f3g4h5i6j7k8l9m0n1o2p3q4r5s6t7u8v9w0x1y2";

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param user Detalles del usuario autenticado
     * @return Token JWT como String
     */
    @Override
    public String getToken(UserDetails user) {
        UserEntity userEntity = userService.getUserByEmail(user.getUsername());

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", userEntity.getName());
        extraClaims.put("id", userEntity.getId());
        extraClaims.put("authorities", List.of("ROLE_" + userEntity.getRoleEntity().getName()));

        return getToken(extraClaims, user);
    }

    /**
     * Genera un token JWT con claims adicionales.
     *
     * @param extraClaims Mapa de claims adicionales a incluir en el token
     * @param user Detalles del usuario autenticado
     * @return Token JWT como String
     */
    @Override
    public String getToken(HashMap<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 minutos de expiración
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene la clave de firma para los tokens.
     *
     * @return Clave de firma como objeto Key
     */
    @Override
    public Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extrae el nombre de usuario (email) de un token JWT.
     *
     * @param token Token JWT
     * @return Email del usuario
     */
    @Override
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Valida si un token JWT es válido para un usuario dado.
     *
     * @param token Token JWT a validar
     * @param userDetails Detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Obtiene todos los claims de un token JWT.
     *
     * @param token Token JWT
     * @return Objeto Claims con toda la información del token
     */
    @Override
    public Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene un claim específico de un token JWT.
     *
     * @param token Token JWT
     * @param claimsResolver Función para extraer el claim deseado
     * @param <T> Tipo del claim a extraer
     * @return Valor del claim solicitado
     */
    @Override
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene la fecha de expiración de un token JWT.
     *
     * @param token Token JWT
     * @return Fecha de expiración del token
     */
    @Override
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token Token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    @Override
    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     * Genera un token de verificación para confirmación de email.
     *
     * @param email Email del usuario a verificar
     * @return Token JWT como String con validez de 48 horas
     */
    @Override
    public String generateVerificationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)) // 48 horas
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera un token para restablecimiento de contraseña.
     *
     * @param email Email del usuario que solicita el restablecimiento
     * @return Token JWT como String con validez de 2 horas
     */
    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 horas
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}