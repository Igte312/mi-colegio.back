package com.micolegio.domain.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComparacionResponse {
    private List<ComercioComparacion> comparacion;
    private String resumen;
}
