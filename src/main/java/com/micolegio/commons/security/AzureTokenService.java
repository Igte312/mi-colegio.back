package com.micolegio.commons.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
public class AzureTokenService {

    private final JwtDecoder jwtDecoder;

    public AzureTokenService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public Jwt validateToken(String token) {
        return jwtDecoder.decode(token);
    }

    public String getEmail(Jwt jwt) {

        String email = jwt.getClaimAsString("email");

        if (email == null) {
            email = jwt.getClaimAsString("unique_name");
        }

        return email;
    }
}

