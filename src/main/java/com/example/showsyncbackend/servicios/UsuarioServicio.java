package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // Lombok genera el constructor con el repositorio
public class UsuarioServicio implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Aquí podrías implementar la lógica de búsqueda de un usuario por nombre de usuario o correo electrónico.
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    public void guardarUsuario(Usuario usuario) {
        // Aquí puedes agregar la lógica para validar el usuario antes de guardarlo. Ejemplo, verificar si el email ya existe. Cifrar la contraseña.
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(usuario.getContrasenya());
        usuario.setContrasenya(encodedPassword);
        usuarioRepositorio.save(usuario);
    }
}
