package com.example.showsyncbackend.servicios;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
 @AllArgsConstructor
public class UsuarioServicio implements UserDetailsService {

    /***SEGUIR CÓDIGO DE https://github.com/ali-bouali/spring-boot-3-jwt-security**/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
