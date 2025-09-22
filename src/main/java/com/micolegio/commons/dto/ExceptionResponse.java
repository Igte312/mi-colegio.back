package com.micolegio.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse extends GenericResponse {
    private String error;

    public ExceptionResponse(int code, String message, String traceId, String error) {
        super(code, message, traceId);
        this.error = error;
    }
}