package com.micolegio.domain.service.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseSupplyListRequest {
    private Long courseId;
    private List<SupplyItem> supplies;
}