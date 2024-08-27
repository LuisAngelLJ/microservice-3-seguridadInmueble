package com.la.microservice_api_gateway.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

public class SecurityUtils {
    //convertir los roles de mi bs a roles de spring
    public static final String ROLE_PREFIX = "ROLE_";

    //jwtprovider
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_TOKEN_TYPE = "Bearer";
    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";

    //concatenacion de datos
    public static SimpleGrantedAuthority convertToAuthoritie(String role) {
        //si exite el prfijo no hacer nada, sino exite concayenarlo
        String formattedRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
        System.out.println("convertToAuthoritie " + formattedRole);
        return new SimpleGrantedAuthority(formattedRole);
    }

    //extraer token de header
    public static String extractAuthTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        //comprobar si el cliente esta mandando el bearer token y que le token comience con AUTH_TOKEN_PREFIX
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
            return bearerToken.substring(7);//7 para que corte después del espacio y solo me devuelva el token
        }

        return null;
    }
}
