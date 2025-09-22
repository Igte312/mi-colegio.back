package com.micolegio.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> extends GenericResponse {
    private T data;
    private Object pagination;

    public SuccessResponse(int code, String message, String traceId, T data, Object pagination) {
        super(code, message, traceId);
        this.data = data;
        this.pagination = pagination;
    }

    // Constructor sin pagination para simplificar
    public SuccessResponse(int code, String message, String traceId, T data) {
        super(code, message, traceId);
        this.data = data;
        this.pagination = null;
    }
}