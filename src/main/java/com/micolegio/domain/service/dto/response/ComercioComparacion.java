package com.micolegio.domain.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComercioComparacion {
    private Integer ranking;
    private String comercio;
    private String url;
    private Boolean tieneDelivery;
    private String precioTotal;
    private String costoDelivery;
    private String observaciones;
}
