package com.la.microservice_api_gateway.service;

import com.la.microservice_api_gateway.model.Role;
import com.la.microservice_api_gateway.model.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    //actualizar rol del usuario
    void changeRole(Role newRole, String username);

    //busqueda de un usuario por username y me retorne el usuario con el token
    User findByUsernameReturnToken(String username);

    //busqueda de un usuario por email y me retorne el usuario con el token
    User findByEmailReturnToken(String email);
}
