package com.micolegio.commons.security;

import com.micolegio.domain.service.user.IUserService;
import com.micolegio.adapters.db.dto.UserBasicInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class CurrentUserInterceptor implements HandlerInterceptor {

    private final AzureTokenService azureTokenService;
    private final IUserService userService;

    public static final String ATTR_USER = "currentUser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token faltante");
            return false;
        }

        String token = authHeader.substring(7);
        var jwt = azureTokenService.validateToken(token);
        String email = azureTokenService.getEmail(jwt);

        UserBasicInfo user = userService.getUserBasicInfoByEmail(email);
        if(user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
            return false;
        }

        // lo guardamos en request para que los controllers lo usen
        request.setAttribute(ATTR_USER, user);
        return true;
    }
}
