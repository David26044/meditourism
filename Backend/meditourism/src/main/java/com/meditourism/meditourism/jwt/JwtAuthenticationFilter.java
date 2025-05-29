package com.meditourism.meditourism.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private UserDetailsService userDetailService;

    // Autowire ObjectMapper to convert Map to JSON
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDateTime
    }

    /**
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/auth") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui.html") ||
                path.startsWith("/email") ||
                path.startsWith("/reviews") ||
                path.startsWith("/clinics") ||
                path.startsWith("/treatments") ||
                path.startsWith("/clinics-treatments")) {

            filterChain.doFilter(request, response);
            return;
        }

        final String token = getTokenFromRequest(request);
        if (token == null) {
            // Token is missing, send 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Token JWT faltante en la cabecera de autorización.");
            return;
        }

        final String email;
        try {
            email = jwtService.getUsernameFromToken(token);
        } catch (Exception e) {
            // Token is invalid or expired, send 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "El token JWT proporcionado es inválido o ha expirado.");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(email);
            if (jwtService.isTokenValid(token, userDetails)) {
                var claims = jwtService.getAllClaims(token);
                var authoritiesFromToken = (List<String>) claims.get("authorities");

                var grantedAuthorities = authoritiesFromToken.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, grantedAuthorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // Token is not valid for the user, send 401 Unauthorized
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "El token JWT no es válido para este usuario.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Sends a consistent JSON error response directly from the filter.
     * @param response The HttpServletResponse to write to.
     * @param status The HTTP status to set (e.g., HttpServletResponse.SC_UNAUTHORIZED).
     * @param error A general error message (e.g., "Unauthorized").
     * @param message A more specific message explaining the error.
     * @throws IOException If an I/O error occurs.
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // Ensure proper character encoding

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);

        // Convert the map to JSON string and write to the response
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}