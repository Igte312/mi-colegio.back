package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.CurrentUser;
import com.micolegio.domain.SchoolSupply;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import com.micolegio.domain.service.schoolSupply.ISchoolSupplyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school-supply")
@AllArgsConstructor
public class SchoolSupplyController {

    private final ISchoolSupplyService schoolSupplyService;

    @GetMapping("/active")
    public List<SchoolSupply> getGeneralActiveSupplies() {
        return schoolSupplyService.getGeneralActiveSupplies();
    }

    @GetMapping("/course/{courseId}")
    public List<CourseSupplyResponse> getSuppliesByCourseId(
            @CurrentUser UserBasicInfo user,
            @PathVariable Long courseId) {
        return schoolSupplyService.getSuppliesByCourseId(user.getSchoolId(), courseId);
    }
}
