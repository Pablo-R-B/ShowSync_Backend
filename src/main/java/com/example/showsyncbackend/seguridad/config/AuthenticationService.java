package com.example.showsyncbackend.seguridad.config;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder; // << Añadido como dependencia

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    public void guardarUsuario(Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(encodedPassword);
        usuarioRepositorio.save(usuario);
    }

    public String autenticarUsuario(String email, String contrasena) {
        System.out.println("=== DEBUG INICIO ===");
        System.out.println("Contraseña recibida (plain): " + contrasena);

        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("Contraseña en BBDD (hash): " + usuario.getContrasena());
        System.out.println("Longitud hash: " + usuario.getContrasena().length());

        boolean match = passwordEncoder.matches(contrasena, usuario.getContrasena());
        System.out.println("Resultado comparación: " + match);

        if (!match) {
            // Debug adicional para contraseñas similares
            System.out.println("Comparando con variantes:");
            System.out.println("Trimmed: " + passwordEncoder.encode(contrasena.trim()));
            System.out.println("Sin !: " + passwordEncoder.encode(contrasena.replace("!", "")));
            throw new RuntimeException("Credenciales incorrectas");
        }

        return jwtService.generateToken(usuario);
    }

    // Añade esto temporalmente en tu AuthenticationService
    @PostConstruct
    public void init() {
        System.out.println("==== ALGORITMO DE ENCRIPTACIÓN ====");
        System.out.println("Encoder class: " + passwordEncoder.getClass().getName());
        System.out.println("Ejemplo de hash: " + passwordEncoder.encode("test123"));
    }



}

