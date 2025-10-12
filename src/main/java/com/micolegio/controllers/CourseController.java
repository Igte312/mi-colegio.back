package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.AzureTokenService;
import com.micolegio.commons.security.CurrentUser;
import com.micolegio.domain.service.course.ICourseService;
import com.micolegio.domain.service.dto.response.CourseListResponse;
import com.micolegio.domain.service.user.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final ICourseService courseService;
    private final IUserService userService;
    private final AzureTokenService azureTokenService;

    @GetMapping("/school")
    public ResponseEntity<List<CourseListResponse>> getCourses(@CurrentUser UserBasicInfo user) {
        List<CourseListResponse> courses = courseService.getCoursesBySchool(user.getSchoolId());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/schoolprueba")
    public ResponseEntity<String> getCourses2() {

        return ResponseEntity.ok("hola con token");
    }
}
