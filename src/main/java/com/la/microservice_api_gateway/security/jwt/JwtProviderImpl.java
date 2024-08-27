package com.la.microservice_api_gateway.security.jwt;

import com.la.microservice_api_gateway.model.User;
import com.la.microservice_api_gateway.security.UserPrincipal;
import com.la.microservice_api_gateway.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProviderImpl implements JwtProvider {
    //leer datos jwt de application.properties
    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION;

    //crear token
    @Override
    public String generateToken(UserPrincipal auth) {
        //recibir los authorities(roles) que tiene el usuario
        String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));//retorna una cadena se string separados por coma

        //des-encriptar token
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities) //con claim se define el payload
                .claim("userId", auth.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512) //firma
                .compact();
    }

    //extraer todos los valores del token
    private Claims extractClaims(HttpServletRequest request) {
        //el metodo util evalua si la autorización es valida y extrae el token
        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        if (token == null) {
            //System.out.println("token no presente");
            return null;
        }

        //des-encriptar el token
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            //System.out.println("Claims extraídos: " + claims);
            return claims;
        } catch (Exception e) {
            System.out.println("Error al decodificar el token: " + e.getMessage());
            return null;
        }
    }

    //obtener los datos del token
    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        //extraer los clains del token
        Claims claims = extractClaims(request);

        if (claims == null) {
            //System.out.println("Claims no extraídos del token");
            return null;
        }

        //obtener los datos del usuario en sesion
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        //saber que tipo de rol tienen
        String rolesClaim = claims.get("roles", String.class);
        System.out.println("rol de usuario " + rolesClaim);
        if (rolesClaim == null) {
            //System.out.println("El claim 'roles' es nulo");
            return null;
        }
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))//convierte el valor de "roles" a una cadena y luego la divide en un arreglo de cadenas utilizando la coma como separador
                .map(SecurityUtils::convertToAuthoritie)//función que concatena ROLE_
                .collect(Collectors.toSet());//recopila los resultados del mapeo en un conjunto (Set).Esto asegura que no haya duplicados en los roles, ya que un conjunto no permite elementos repetidos.


        //crear objeto de tipo usuario
        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        //crear objeto Authentication
        if (username == null) {
            //System.out.println("El claim 'username' es nulo");
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    //saber si el token es valido
    @Override
    public boolean isTokenValid(HttpServletRequest request) {
        Claims claims = extractClaims(request);
        if (claims == null) {
            //System.out.println("Claims no válidos");
            return false;
        }
        //si la fecha ya expiro
        if (claims.getExpiration().before(new Date())) {
            //System.out.println("Token no valido");
            return false;
        }

        //System.out.println("Token es válido");
        return true;
    }

    @Override
    public String generateToken(User user) {
        Key key =  Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRole()) //con claim se define el payload
                .claim("userId", user.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512) //firma - codificar
                .compact();
    }
}
