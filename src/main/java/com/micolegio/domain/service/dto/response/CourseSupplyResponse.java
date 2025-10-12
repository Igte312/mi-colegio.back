package com.micolegio.domain.service.dto.response;

import lombok.Data;

@Data
public class CourseSupplyResponse {
    private Long schoolSupplyId;
    private Long courseId;
    private String name;
    private String description;
    private Integer quantity;
}
