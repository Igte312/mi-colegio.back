package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.AzureTokenService;
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
    public ResponseEntity<List<CourseListResponse>> getCourses(
            @RequestHeader("Authorization") String authHeader) {

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        var jwt = azureTokenService.validateToken(token);
        String email = azureTokenService.getEmail(jwt);

        UserBasicInfo user = userService.getUserBasicInfoByEmail(email);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<CourseListResponse> courses = courseService.getCoursesBySchool(user.getSchoolId());
        return ResponseEntity.ok(courses);
    }
}
