package com.example.showsyncbackend.seguridad.config.dto;

import com.example.showsyncbackend.enumerados.Rol;
import lombok.Data;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class UsuarioRegistroDTO {
    private String nombreUsuario;
    private String email;
    private String contrasenya;
    private LocalDate fechaNacimiento;
    private Rol rol;

    // Métodos de validación manual
    public boolean esValido() {
        return esNombreUsuarioValido() && esEmailValido() && esContrasenyaValida() && esFechaNacimientoValida() && esRolValido();
    }

    private boolean esNombreUsuarioValido() {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean esEmailValido() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Usamos una expresión regular para validar el formato del email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean esContrasenyaValida() {
        if (contrasenya == null || contrasenya.length() < 8) { // Contraseña mínima de 8 caracteres
            return false;
        }
        // Validar que tenga al menos un número, una letra mayúscula y un carácter especial
        boolean tieneNumero = false;
        boolean tieneMayuscula = false;
        boolean tieneCaracterEspecial = false;

        for (char c : contrasenya.toCharArray()) {
            if (Character.isDigit(c)) tieneNumero = true;
            if (Character.isUpperCase(c)) tieneMayuscula = true;
            if (!Character.isLetterOrDigit(c)) tieneCaracterEspecial = true;
        }

        return tieneNumero && tieneMayuscula && tieneCaracterEspecial;
    }

    private boolean esFechaNacimientoValida() {
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            return false;
        }
        // Validar que la edad mínima sea de 18 años
        LocalDate fechaMinima = LocalDate.now().minusYears(18);
        return !fechaNacimiento.isAfter(fechaMinima);
    }

    private boolean esRolValido() {
        return rol != null;
    }
}
