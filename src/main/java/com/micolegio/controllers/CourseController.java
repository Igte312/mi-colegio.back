package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.CurrentUserInterceptor;
import com.micolegio.domain.service.course.ICourseService;
import com.micolegio.domain.service.dto.response.CourseListResponse;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/school")
    public List<CourseListResponse> getCoursesForMySchool(HttpServletRequest request){
        UserBasicInfo user = (UserBasicInfo) request.getAttribute(CurrentUserInterceptor.ATTR_USER);
        return courseService.getCoursesBySchool(user.getSchoolId());
    }
}
