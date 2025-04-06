package com.example.showsyncbackend.seguridad.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Key;

import static javax.crypto.Cipher.SECRET_KEY;

@Configuration
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;




    public SecurityConfig(JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilita CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Usa autenticación sin estado
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/registro", "/test").permitAll()  // Permite acceso a estas rutas sin autenticación
                        .anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Añade el filtro JWT

        return http.build();
    }





}
