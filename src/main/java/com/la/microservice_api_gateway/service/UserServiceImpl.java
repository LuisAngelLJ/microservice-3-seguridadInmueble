package com.la.microservice_api_gateway.service;

import com.la.microservice_api_gateway.model.Role;
import com.la.microservice_api_gateway.model.User;
import com.la.microservice_api_gateway.repository.UserRepository;
import com.la.microservice_api_gateway.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; //llamo al bean declarado en la clase principal del proyecto

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setFechaCreacion(LocalDateTime.now());
        User userCreated = userRepository.save(user);
        //crear token
        String jwt = jwtProvider.generateToken(userCreated);
        //asignar token al objeto user
        userCreated.setToken(jwt);
        return userCreated;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //actualizar rol del usuario
    @Transactional //ponerlo en metodos donde uso sql nativo
    @Override
    public void changeRole(Role newRole, String username) {
        userRepository.updateUserRole(username, newRole);
    }

    //busqueda de un usuario por username y me retorne el usuario con el token
    @Override
    public User findByUsernameReturnToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no existe " + username));

        String jwt = jwtProvider.generateToken(user);
        user.setToken(jwt);
        return user;
    }

    //busqueda de un usuario por email y me retorne el usuario con el token
    @Override
    public User findByEmailReturnToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El email no existe " + email));

        String jwt = jwtProvider.generateToken(user);
        user.setToken(jwt);
        return user;
    }
}
