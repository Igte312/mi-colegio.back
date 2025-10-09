package com.micolegio.commons.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class AzureJwtConfig {

    private static final String TENANT_ID = "28610735-5d7e-4a84-94af-10533012ed60";

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "https://login.microsoftonline.com/" + TENANT_ID + "/discovery/v2.0/keys";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}

