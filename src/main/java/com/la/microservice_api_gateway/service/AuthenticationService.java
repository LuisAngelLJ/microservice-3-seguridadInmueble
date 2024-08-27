package com.la.microservice_api_gateway.service;

import com.la.microservice_api_gateway.model.User;

public interface AuthenticationService {
    User singInAndReturnJWT(User singInRequest);
}
