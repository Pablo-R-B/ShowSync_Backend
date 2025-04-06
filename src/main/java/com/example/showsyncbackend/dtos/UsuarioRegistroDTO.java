package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.enumerados.Rol;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioRegistroDTO {
    private String nombreUsuario;
    private String email;
    private String contrasenya;
    private LocalDate fechaNacimiento;
    private Rol rol;

    // Métodos de validación manual
    public boolean esValido() {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return false;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return false;
        }
        if (contrasenya == null || contrasenya.length() < 6) { // ejemplo de longitud mínima
            return false;
        }
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            return false;
        }
        if (rol == null) {
            return false;
        }
        return true;
    }
}
