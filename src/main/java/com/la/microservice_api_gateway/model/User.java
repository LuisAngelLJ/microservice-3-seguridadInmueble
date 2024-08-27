package com.la.microservice_api_gateway.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)//que tipo de dato es
    @Column(name = "role", nullable = false)
    private Role role;

    @Transient //alamacenamiento temporal
    private String token;
}
