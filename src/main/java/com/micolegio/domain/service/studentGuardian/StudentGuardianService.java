package com.micolegio.domain.service.studentGuardian;

import com.micolegio.adapters.db.IStudentGuardianRepository;
import com.micolegio.commons.exception.BadRequestException;
import com.micolegio.commons.exception.NotFoundException;
import com.micolegio.domain.service.dto.request.StudentGuardianCsvRequest;
import com.micolegio.domain.service.dto.response.StudentGuardianResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentGuardianService implements IStudentGuardianService {

    private final IStudentGuardianRepository studentGuardianRepository;

    @Override
    @Transactional
    public void processStudentGuardianCsv(StudentGuardianCsvRequest request) {
        Long courseId = request.getCourseId();

        // Validate that course exists
        if (!((com.micolegio.adapters.db.StudentGuardianRepository) studentGuardianRepository).courseExists(courseId)) {
            throw new NotFoundException("Course with ID " + courseId + " not found");
        }

        // Validate that data is not empty
        if (request.getData() == null || request.getData().isEmpty()) {
            throw new BadRequestException("CSV data cannot be empty");
        }

        // Delete existing data in correct order to avoid foreign key violations
        studentGuardianRepository.deleteStudentGuardiansByCourseId(courseId);
        studentGuardianRepository.deleteStudentCoursesByCourseId(courseId);
        studentGuardianRepository.deletePersonsByCourseId(courseId);

        // Insert new data
        studentGuardianRepository.insertStudentGuardianData(courseId, request.getData());

        // Clean up orphaned records
        studentGuardianRepository.deleteOrphanedStudents();
        studentGuardianRepository.deleteOrphanedGuardians();
    }

    @Override
    public List<StudentGuardianResponse> getStudentsAndGuardiansByCourseId(Long courseId) {
        // Validate that course exists
        if (!((com.micolegio.adapters.db.StudentGuardianRepository) studentGuardianRepository).courseExists(courseId)) {
            throw new NotFoundException("Course with ID " + courseId + " not found");
        }

        return studentGuardianRepository.findStudentsAndGuardiansByCourseId(courseId);
    }
}