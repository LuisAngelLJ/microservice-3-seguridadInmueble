package com.la.microservice_api_gateway.controller;

import com.la.microservice_api_gateway.request.CompraServiceRequest;
import com.la.microservice_api_gateway.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gateway/compra")
public class CompraController {
    @Autowired
    private CompraServiceRequest compraServiceRequest;

    @PostMapping
    public ResponseEntity<?> saveCompra(@RequestBody Object compra) {
        return new ResponseEntity<>(this.compraServiceRequest.saveCompra(compra), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllComprasOfUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(this.compraServiceRequest.getAllComprasForUser(userPrincipal.getId()));
    }
}
