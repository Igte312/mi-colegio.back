package com.micolegio.domain.service.course;

import com.micolegio.domain.service.dto.response.CourseListResponse;

import java.util.List;

public interface ICourseService {

    List<CourseListResponse> getCoursesBySchool(Long schoolId);
}
