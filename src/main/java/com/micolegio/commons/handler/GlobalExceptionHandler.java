package com.micolegio.commons.handler;

import com.micolegio.commons.dto.ExceptionResponse;
import com.micolegio.commons.exception.BadRequestException;
import com.micolegio.commons.exception.NotFoundException;
import com.micolegio.commons.exception.TooManyRequestsException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(1)
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // handler BadRequest: validations and invalid JSON
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = buildBadRequestMessage(ex);
        String errorClass = ex.getClass().getSimpleName();

        logError(request, ex, status);
        return buildErrorResponse(status, message, errorClass);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        logError(request, ex, HttpStatus.NOT_FOUND);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        logError(request, ex, HttpStatus.BAD_REQUEST);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ExceptionResponse> handleTooManyRequests(TooManyRequestsException ex, HttpServletRequest request) {
        logError(request, ex, HttpStatus.TOO_MANY_REQUESTS);
        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        logError(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", ex.getClass().getSimpleName());
    }

    // =========================
    //      Private Helpers
    // =========================

    private String buildBadRequestMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            List<String> errors = validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            return String.join("; ", errors);
        }

        if (ex instanceof HttpMessageNotReadableException msgEx) {
            if (msgEx.getCause() instanceof InvalidFormatException invalidEx) {
                String field = invalidEx.getPath().stream()
                        .map(JsonMappingException.Reference::getFieldName)
                        .collect(Collectors.joining("."));
                return "Invalid value for field '" + field + "': " + invalidEx.getValue();
            }
            return "Request body is invalid";
        }

        return "Request body is invalid";
    }

    private void logError(HttpServletRequest request, Exception ex, HttpStatus status) {
        logger.error("[{}] {} - Status:{} - Message: {}",
                request.getMethod(), request.getRequestURI(), status.value(), ex.getMessage(), ex);
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(HttpStatus status, String message, String error) {
        String traceId = (String) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
                .getAttribute("trace-id", RequestAttributes.SCOPE_REQUEST);

        return new ResponseEntity<>(
                new ExceptionResponse(status.value(), message, traceId, error),
                status
        );
    }
}
