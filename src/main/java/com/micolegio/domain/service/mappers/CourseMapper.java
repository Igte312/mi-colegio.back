package com.micolegio.domain.service.mappers;

import com.micolegio.domain.Course;
import com.micolegio.domain.service.dto.response.CourseListResponse;

public class CourseMapper {

    private CourseMapper() {}

    public static CourseListResponse toCourseListResponse(Course course) {
        return CourseListResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .level(course.getLevel())
                .letter(course.getLetter())
                .schoolId(course.getSchoolId())
                .build();
    }
}
