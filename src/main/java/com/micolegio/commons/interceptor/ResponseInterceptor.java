package com.micolegio.commons.interceptor;

import com.micolegio.commons.dto.ExceptionResponse;
import com.micolegio.commons.dto.SuccessResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice<Object> {

    private String getTraceId() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            Object trace = attr.getRequest().getAttribute("trace-id");
            return trace != null ? trace.toString() : "";
        }
        return "";
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // Excluir StringHttpMessageConverter específicamente
        return !converterType.getSimpleName().equals("StringHttpMessageConverter");
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        // Si ya es una respuesta estructurada, no hacer nada
        if (body instanceof ExceptionResponse || body instanceof SuccessResponse) {
            return body;
        }

        // Ignorar rutas específicas de documentación y monitoreo
        String path = request.getURI().getPath();
        if (path.contains("/swagger") || path.contains("/api-docs") || path.contains("/actuator") || path.contains("/health")) {
            return body;
        }

        // Extraer paginación si es una Page de Spring
        Object pagination = null;
        Object data = body;

//        if (body instanceof org.springframework.data.domain.Page<?> page) {
//            pagination = Map.of(
//                    "page", page.getNumber(),
//                    "size", page.getSize(),
//                    "totalElements", page.getTotalElements(),
//                    "totalPages", page.getTotalPages()
//            );
//            data = page.getContent();
//        }

        // Crear respuesta exitosa estructurada
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "Successful",
                getTraceId(),
                data,
                pagination
        );
    }
}