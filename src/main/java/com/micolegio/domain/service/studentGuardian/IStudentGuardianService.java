package com.micolegio.domain.service.studentGuardian;

import com.micolegio.domain.service.dto.request.StudentGuardianCsvRequest;
import com.micolegio.domain.service.dto.response.StudentGuardianResponse;

import java.util.List;

public interface IStudentGuardianService {

    void processStudentGuardianCsv(StudentGuardianCsvRequest request);

    List<StudentGuardianResponse> getStudentsAndGuardiansByCourseId(Long courseId);
}