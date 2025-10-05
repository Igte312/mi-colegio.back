package com.micolegio.controllers;

import com.micolegio.domain.service.course.ICourseService;
import com.micolegio.domain.service.dto.response.CourseListResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping("/school/{schoolId}")
    public List<CourseListResponse> getCoursesBySchool(@PathVariable Long schoolId){
        return courseService.getCoursesBySchool(schoolId);
    }
}
