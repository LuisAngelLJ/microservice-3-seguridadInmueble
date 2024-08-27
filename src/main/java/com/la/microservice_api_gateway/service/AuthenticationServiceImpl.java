package com.la.microservice_api_gateway.service;

import com.la.microservice_api_gateway.model.User;
import com.la.microservice_api_gateway.security.UserPrincipal;
import com.la.microservice_api_gateway.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User singInAndReturnJWT(User singInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(singInRequest.getUsername(), singInRequest.getPassword()));

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal);
        User singUser = userPrincipal.getUser();
        singUser.setToken(jwt);
        return singUser;
    }
}
