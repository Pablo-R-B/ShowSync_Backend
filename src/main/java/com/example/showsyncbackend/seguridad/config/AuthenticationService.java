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
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        boolean match = passwordEncoder.matches(contrasena, usuario.getContrasena());

        if (!match) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return jwtService.generateToken(loadUserByUsername(email));
    }



}

