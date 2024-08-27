package com.la.microservice_api_gateway.controller;

import com.la.microservice_api_gateway.request.InmuebleServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gateway/inmueble")
public class InmuebleController {
    //usar la interface de request
    @Autowired
    private InmuebleServiceRequest inmuebleServiceRequest;

    @PostMapping
    public ResponseEntity<?> saveInmueble(@RequestBody Object inmueble) {
        return new ResponseEntity<>(this.inmuebleServiceRequest.saveInmueble(inmueble), HttpStatus.CREATED);
    }

    @DeleteMapping("{inmuebleId}")
    public ResponseEntity<?> deleteInmueble(@PathVariable Long inmuebleId) {
        this.inmuebleServiceRequest.deleteInmueble(inmuebleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> gelAllInmuebles() {
        return ResponseEntity.ok(this.inmuebleServiceRequest.getAllInmuebles());
    }
}
