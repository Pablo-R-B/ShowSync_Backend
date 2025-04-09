package com.example.showsyncbackend.seguridad.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



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
                        .requestMatchers("/auth/**").permitAll()  // Permite acceso a estas rutas sin autenticación
                        .anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Añade el filtro JWT

        return http.build();
    }


}
