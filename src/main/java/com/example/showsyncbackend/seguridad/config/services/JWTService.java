package com.example.showsyncbackend.seguridad.config.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    // Extraer el nombre de usuario del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer una reclamación específica del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generar un token para un usuario
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    // Generar un token con reclamaciones personalizadas
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar el token con el nombre de usuario del usuario
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    // Extraer la fecha de expiración del token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraer todas las reclamaciones del token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Método para generar la clave secreta de firma
    private Key getSignInKey() {
        byte[] keyBytes;
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            // Generar una clave segura de 256 bits
            keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        } else {
            // Si la clave está configurada, validamos que tenga al menos 256 bits
            keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("La clave secreta debe tener al menos 256 bits.");
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Configuración personalizada para Jackson
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return Jackson2ObjectMapperBuilder.json().featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
