package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.CurrentUser;
import com.micolegio.domain.service.dto.request.StudentGuardianCsvRequest;
import com.micolegio.domain.service.dto.response.StudentGuardianResponse;
import com.micolegio.domain.service.studentGuardian.IStudentGuardianService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-guardian")
@AllArgsConstructor
public class StudentGuardianController {

    private final IStudentGuardianService studentGuardianService;

    @PostMapping("/upload-csv")
    public ResponseEntity<Void> uploadStudentGuardianCsv(
            @Valid @RequestBody StudentGuardianCsvRequest request,
            @CurrentUser UserBasicInfo user) {
        studentGuardianService.processStudentGuardianCsv(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentGuardianResponse>> getStudentsAndGuardiansByCourseId(
            @PathVariable Long courseId,
            @CurrentUser UserBasicInfo user) {
        List<StudentGuardianResponse> response = studentGuardianService.getStudentsAndGuardiansByCourseId(courseId);
        return ResponseEntity.ok(response);
    }
}