package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.exception.NotFoundException;
import com.micolegio.commons.security.AzureTokenService;
import com.micolegio.commons.security.TokenService;
import com.micolegio.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AzureTokenService azureTokenService;
    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/token")
    public String getInternalToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Header Authorization inválido");
        }

        String azureToken = authHeader.substring(7);

        Jwt jwt = azureTokenService.validateToken(azureToken);

        String email = azureTokenService.getEmail(jwt);

        UserBasicInfo user = userService.getUserBasicInfoByEmail(email);
        if (user == null) {
            throw new NotFoundException("Usuario no encontrado en DB");
        }

        return tokenService.generateInternalToken(user);
    }
}

