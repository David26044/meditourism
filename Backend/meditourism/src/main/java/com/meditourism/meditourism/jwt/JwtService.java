package com.meditourism.meditourism.jwt;

import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.websocket.servlet.TomcatWebSocketServletWebServerCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService implements IJwtService {

    @Autowired
    IUserService userService;

    // ✅ Clave secreta segura (256 bits - 32 caracteres)
    private static final String SECRET_KEY = "xU2jR7qZ8vGfT5kL0pWmNsEyAbCqHdXi";

//    // 🔐 Convertimos el string a una clave válida para HS256
//    private static final SecretKey SECRET_KEY = new SecretKeySpec(
//            SECRET.getBytes(StandardCharsets.UTF_8),
//            SignatureAlgorithm.HS256.getJcaName()
//    );
//    @Autowired
//    private TomcatWebSocketServletWebServerCustomizer tomcatWebSocketServletWebServerCustomizer;
//
//    @Override
//    public String generateToken(String email) {
//        UserEntity user = userService.getUserByEmail(email);
//        return Jwts.builder()
//                .setSubject(String.valueOf(user.getId()))
//                .claim("email", user.getEmail())
//                .claim("name", user.getName()) // Ajusta el nombre del atributo
//                .claim("roleId", user.getRoleEntity().getId())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
//                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
//                .compact();
//    }

    /**
     * @param email 
     * @return
     */
    @Override
    public String generateToken(String email) {
        return "";
    }

    /**
     * @param user 
     * @return
     */
    @Override
    public String getToken(UserDetails user) {
       return getToken(new HashMap<>(), user);
    }

    private String getToken(HashMap<String,Object> extratcClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extratcClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] KeyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }


}
