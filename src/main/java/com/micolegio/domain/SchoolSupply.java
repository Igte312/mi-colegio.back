package com.micolegio.domain;

import lombok.Data;

@Data
public class SchoolSupply {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
}