package com.meditourism.meditourism.jwt;

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
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private UserDetailsService userDetailService;

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 🚫 Ignorar el filtro para el login y otros endpoints públicos
        String path = request.getServletPath();
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = getTOkenFromRequest(request);
        final String email;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            email = jwtService.getUsernameFromToken(token);
        } catch (Exception e) {
            // Si el token no es válido, no continuar
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
            }
        }

        filterChain.doFilter(request, response);
    }


    private String getTOkenFromRequest(HttpServletRequest request) {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}
