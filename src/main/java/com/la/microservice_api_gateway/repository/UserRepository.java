package com.la.microservice_api_gateway.repository;

import com.la.microservice_api_gateway.model.Role;
import com.la.microservice_api_gateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //buscar el usuario por username
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    //actualizar datos por rol
    @Modifying //especificar que se van a hacer cambios
    @Query("update User set role=:role where username=:username")
    void updateUserRole(@Param("username") String username, @Param("role") Role rol);
}
