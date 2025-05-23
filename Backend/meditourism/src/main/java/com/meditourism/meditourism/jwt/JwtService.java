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
import java.util.function.Function;

@Service
public class JwtService implements IJwtService {

    @Autowired
    IUserService userService;

    // ✅ Clave secreta segura (256 bits - 32 caracteres)
    private static final String SECRET = "4f8c1e2b3d5f6a7e8b9c0d1e2f3g4h5i6j7k8l9m0n1o2p3q4r5s6t7u8v9w0x1y2"; // Asegúrate de que esta clave tenga al menos 32 caracteres



    @Override
    public String getToken(UserDetails user) {
        UserEntity userEntity = userService.getUserByEmail(user.getUsername());

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", userEntity.getName());
        extraClaims.put("id", userEntity.getId());
        extraClaims.put("roleId", userEntity.getRoleEntity().getId());

        return getToken(extraClaims, user);
    }

    @Override
    public String getToken(HashMap<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256) // Usa la clave segura
                .compact();
    }

    @Override
    public Key getKey() {
        // Genera una clave segura de 256 bits para HMAC-SHA
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName=getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    @Override
    public String generateVerificationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}
