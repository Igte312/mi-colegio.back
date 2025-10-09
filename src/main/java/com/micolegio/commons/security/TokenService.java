package com.micolegio.commons.security;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    public String generateInternalToken(UserBasicInfo user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roleName", user.getRoleName());
        claims.put("schoolId", user.getSchoolId());
        claims.put("schoolName", user.getSchoolName());
        claims.put("isActive", user.getIsActive());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
}

