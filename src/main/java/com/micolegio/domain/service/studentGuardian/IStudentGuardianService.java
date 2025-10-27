package com.micolegio.domain.service.studentGuardian;

import com.micolegio.domain.service.dto.request.StudentGuardianCsvRequest;

public interface IStudentGuardianService {

    void processStudentGuardianCsv(StudentGuardianCsvRequest request);
}