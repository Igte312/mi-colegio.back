package com.micolegio.adapters.db;

import com.micolegio.domain.service.dto.request.StudentGuardianCsvData;
import com.micolegio.domain.service.dto.response.StudentGuardianResponse;

import java.util.List;

public interface IStudentGuardianRepository {

    void deleteStudentGuardiansByCourseId(Long courseId);

    void deleteStudentCoursesByCourseId(Long courseId);

    void deletePersonsByCourseId(Long courseId);

    void insertStudentGuardianData(Long courseId, List<StudentGuardianCsvData> data);

    void deleteOrphanedStudents();

    void deleteOrphanedGuardians();

    List<StudentGuardianResponse> findStudentsAndGuardiansByCourseId(Long courseId);
}