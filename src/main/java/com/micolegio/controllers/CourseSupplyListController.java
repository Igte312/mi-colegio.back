package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.CurrentUser;
import com.micolegio.domain.service.courseSupplyList.ICourseSupplyListService;
import com.micolegio.domain.service.dto.request.CourseSupplyListRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-supply-list")
@AllArgsConstructor
public class CourseSupplyListController {

    private final ICourseSupplyListService courseSupplyListService;

    @PutMapping("/update")
    public ResponseEntity<Void> updateCourseSupplies(
            @RequestBody CourseSupplyListRequest request,
            @CurrentUser UserBasicInfo user) {
        courseSupplyListService.updateCourseSupplies(request, user.getSchoolId());
        return ResponseEntity.ok().build();
    }
}