package com.example.showsyncbackend.modelos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class Usuario implements UserDetails {


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USUARIO"));
        //CAMBIAR USER POR THIS.ROL.NAME() CUANDO SE CREE EL ENUM ROL Y SE INCLUYA EL ATRIBUTO ROL EN USUARIO
    }

    /**BORRAR GETPASSWORD Y GETUSERNAME CUANDO ESTÉN INCLUIDOS LOS ATRIBUTOS EN LA CLASE**/
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
    /**###########################################**/


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
