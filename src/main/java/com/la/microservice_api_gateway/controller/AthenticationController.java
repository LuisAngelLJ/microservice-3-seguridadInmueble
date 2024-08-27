package com.la.microservice_api_gateway.controller;

import com.la.microservice_api_gateway.model.User;
import com.la.microservice_api_gateway.service.AuthenticationService;
import com.la.microservice_api_gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/authentication")
public class AthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    //registrar usuario
    @PostMapping("/registrar")
    public ResponseEntity<?> singUp(@RequestBody User user) {
        System.out.println("usuario recibido: " + user);
        //validar si el usuario si existe
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        ResponseEntity result = new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);;
        System.out.println("registrar = " + result);
        return result;
    }

    //login
    @PostMapping("/sing-in")
    public ResponseEntity<?> singIn(@RequestBody User user) {
        return new ResponseEntity<>(authenticationService.singInAndReturnJWT(user), HttpStatus.OK);
    }
}
