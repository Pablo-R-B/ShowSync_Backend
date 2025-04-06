package com.example.showsyncbackend.seguridad.config;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final JWTService jwtService; // Añadir el servicio JWT

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    public void guardarUsuario(Usuario usuario) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(usuario.getContrasenya());
        usuario.setContrasenya(encodedPassword);
        usuarioRepositorio.save(usuario);
    }

    // Método para autenticar al usuario y generar el JWT
    public String autenticarUsuario(String email, String contrasenya) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        System.out.println("Usuario encontrado: " + usuario.getEmail()); // Para depuración

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(contrasenya, usuario.getContrasenya())) {
            // Si la contraseña es válida, generar y devolver el token JWT
            return jwtService.generateToken(usuario);
        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }



}
