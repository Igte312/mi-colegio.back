package com.micolegio.domain;

import lombok.Data;

@Data
public class CourseSupplyList {
    private Long id;
    private Long schoolSupplyId;
    private Long courseId;
    private Boolean isActive;
    private Integer quantity;
}