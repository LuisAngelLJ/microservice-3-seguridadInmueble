package com.la.microservice_api_gateway.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        value = "microservice-inmueble",
        path = "/api/inmueble",
        //url = "${inmueble.service.url}",
        configuration = FeigConfiguration.class

)
public interface InmuebleServiceRequest {
    //mapear metodos rest
    @PostMapping
    Object saveInmueble(@RequestBody Object requestBody);

    @DeleteMapping("{inmuebleId}")
    void deleteInmueble(@PathVariable("inmuebleId") Long inmuebleId);

    @GetMapping
    List<Object> getAllInmuebles();
}
