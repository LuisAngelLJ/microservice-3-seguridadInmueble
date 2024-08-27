package com.la.microservice_api_gateway.security;

import com.la.microservice_api_gateway.model.User;
import com.la.microservice_api_gateway.service.UserService;
import com.la.microservice_api_gateway.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    //buscar un usiario y cargarlo en el contexto de seguridad
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //buscar usuario con ayuda de entidad model
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + username));

        //authorities asignar roles de la base de datos
        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthoritie(user.getRole().name()));

        return UserPrincipal.builder()
                .user(user)
                .id(user.getId())
                .username(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build(); //inicializar los valores de esta clase UserPrincipal
    }
}
