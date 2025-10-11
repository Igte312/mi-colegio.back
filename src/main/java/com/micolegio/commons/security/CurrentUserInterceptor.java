package com.micolegio.commons.security;

import com.micolegio.domain.service.user.IUserService;
import com.micolegio.adapters.db.dto.UserBasicInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// 💡 IMPORTACIÓN CORRECTA DE LOGGER (de SLF4J, no java.util.logging)
import org.slf4j.Logger;

@Component
@RequiredArgsConstructor
public class CurrentUserInterceptor implements HandlerInterceptor {

    // 💡 REVISA ESTA LÍNEA: Cambié la conversión a SLF4J Logger
    //private static final Logger log = LoggerFactory.getLogger(CurrentUserInterceptor.class);
    private final AzureTokenService azureTokenService;
    private final IUserService userService;

    public static final String ATTR_USER = "currentUser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true; // deja pasar preflight
        }

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token faltante");
            return false;
        }

        String token = authHeader.substring(7);
        var jwt = azureTokenService.validateToken(token);
        String email = azureTokenService.getEmail(jwt);

        //log.info("🔥 Email extraído del token para buscar en BD: {}", email);

        UserBasicInfo user = userService.getUserBasicInfoByEmail(email);
        if(user == null) {
            //log.warn("❌ Usuario NO encontrado en BD con el email: {}", email);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
            return false;
        }

        request.setAttribute(ATTR_USER, user);
        return true;
    }

}