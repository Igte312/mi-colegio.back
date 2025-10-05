package com.micolegio.domain.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseListResponse {
    private Long id;
    private String name;
    private String level;
    private String letter;
    private Long schoolId;
}
