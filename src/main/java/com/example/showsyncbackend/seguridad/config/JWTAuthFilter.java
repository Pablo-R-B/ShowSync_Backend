package com.example.showsyncbackend.seguridad.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
@AllArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    public void init() {
        System.out.println("JWTAuthFilter inicializado");
    }




    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");



        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header found or invalid format");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // Obtener el token JWT
        System.out.println("JWT Token recibido: " + jwt);
        final String username;
        try {
            username = jwtService.extractUsername(jwt); // Extraer el nombre de usuario del token
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ha expirado");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Si el token es válido y no hay una autenticación actual en el contexto
            var userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(jwt, userDetails)) {
                // Si el token es válido, autenticar al usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response); // Continuar con la siguiente parte de la cadena de filtros
    }
}















//public class JWTAuthFilter extends OncePerRequestFilter {
//
//    private final JWTService jwtService;
//    private final AuthenticationService authenticationService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String username;
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7); // Obtener el token JWT
//        username = jwtService.extractUsername(jwt);
//        try {
//            username = jwtService.extractUsername(jwt); // Extraer el nombre de usuario del token
//        } catch (ExpiredJwtException e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ha expirado");
//            return;
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Si el token es válido y no hay una autenticación actual en el contexto
//            var userDetails = authenticationService.loadUserByUsername(username);
//            if (jwtService.validateToken(jwt, userDetails)) {
//                // Si el token es válido, autenticar al usuario
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails.getUsername(), null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        filterChain.doFilter(request, response); // Continuar con la siguiente parte de la cadena de filtros
//    }
