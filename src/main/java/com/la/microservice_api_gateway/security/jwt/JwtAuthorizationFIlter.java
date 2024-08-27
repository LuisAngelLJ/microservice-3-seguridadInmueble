package com.la.microservice_api_gateway.security.jwt;

import com.la.microservice_api_gateway.utils.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFIlter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;//para que inicialice necesito crear un método Bean en SecurityConfig - jwtAuthorizationFIlter

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = SecurityUtils.extractAuthTokenFromRequest(request);
        if (token == null) {
            System.out.println("Token no presente en la solicitud");
        } else {
            System.out.println("Token presente: " + token);
        }

        Authentication authentication = jwtProvider.getAuthentication(request);
        System.out.println(authentication);
        if (authentication != null && jwtProvider.isTokenValid(request)) {
            //cargar usuario al contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //System.out.println("token no presente o invalido");
        //si esta incorrecto que mande una excepcion
        filterChain.doFilter(request, response);
    }
}
