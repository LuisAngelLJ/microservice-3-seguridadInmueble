package com.la.microservice_api_gateway.security.jwt;

import com.la.microservice_api_gateway.model.User;
import com.la.microservice_api_gateway.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface JwtProvider {
    //crear token
    String generateToken(UserPrincipal auth);

    //obtener los datos del token
    Authentication getAuthentication(HttpServletRequest request);

    //saber si el token es valido
    boolean isTokenValid(HttpServletRequest request);

    String generateToken(User user);
}
