package com.micolegio.commons.security;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.domain.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private final AzureTokenService azureTokenService;
    private final IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(UserBasicInfo.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token ausente o inválido");
        }

        String token = authHeader.substring(7);
        Jwt jwt = azureTokenService.validateToken(token);
        String email = azureTokenService.getEmail(jwt);

        UserBasicInfo user = userService.getUserBasicInfoByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado en base de datos");
        }

        return user;
    }
}
