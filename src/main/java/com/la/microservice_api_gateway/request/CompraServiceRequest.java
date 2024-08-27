package com.la.microservice_api_gateway.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        value = "microservice-compra",
        path = "/api/compra",
        //url = "${compras.service.url}",
        configuration = FeigConfiguration.class
)
public interface CompraServiceRequest {
    @PostMapping
    Object saveCompra(@RequestBody Object requestBody);

    @GetMapping("{userId}")
    List<Object> getAllComprasForUser(@PathVariable("userId") Long userId);
}
