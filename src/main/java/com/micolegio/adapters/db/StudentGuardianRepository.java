package com.micolegio.adapters.db;

import com.micolegio.domain.service.dto.request.StudentGuardianCsvData;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class StudentGuardianRepository implements IStudentGuardianRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteStudentGuardiansByCourseId(Long courseId) {
        String sql = "DELETE FROM public.\"STUDENT_GUARDIAN\" WHERE student_id IN (SELECT student_id FROM public.\"STUDENT_COURSE\" WHERE course_id = ?)";
        jdbcTemplate.update(sql, courseId);
    }

    @Override
    public void deleteStudentCoursesByCourseId(Long courseId) {
        String sql = "DELETE FROM public.\"STUDENT_COURSE\" WHERE course_id = ?";
        jdbcTemplate.update(sql, courseId);
    }

    @Override
    public void deletePersonsByCourseId(Long courseId) {
        String sql = """
            DELETE FROM public."PERSON"
            WHERE id IN (
                SELECT student_id FROM public."STUDENT_COURSE" WHERE course_id = ?
                UNION
                SELECT guardian_id FROM public."STUDENT_GUARDIAN" WHERE student_id IN (
                    SELECT student_id FROM public."STUDENT_COURSE" WHERE course_id = ?
                )
            )
            """;
        jdbcTemplate.update(sql, courseId, courseId);
    }

    @Override
    public void insertStudentGuardianData(Long courseId, List<StudentGuardianCsvData> data) {
        for (StudentGuardianCsvData item : data) {
            // Insert guardian (person_type_id = 3)
            String insertGuardianSql = """
                INSERT INTO public."PERSON" (first_name, last_name, email, created_at, updated_at, is_active, person_type_id)
                VALUES (?, ?, ?, now(), now(), true, 3)
                RETURNING id
                """;
            Long guardianId = jdbcTemplate.queryForObject(insertGuardianSql, Long.class,
                item.getGuardianFirstName(), item.getGuardianLastName(), item.getGuardianEmail());

            // Insert student (person_type_id = 2)
            String insertStudentSql = """
                INSERT INTO public."PERSON" (first_name, last_name, email, created_at, updated_at, is_active, person_type_id)
                VALUES (?, ?, ?, now(), now(), true, 2)
                RETURNING id
                """;
            Long studentId = jdbcTemplate.queryForObject(insertStudentSql, Long.class,
                item.getStudentFirstName(), item.getStudentLastName(), item.getStudentEmail());

            // Insert into STUDENT_COURSE
            String insertStudentCourseSql = """
                INSERT INTO public."STUDENT_COURSE" (student_id, course_id)
                VALUES (?, ?)
                """;
            jdbcTemplate.update(insertStudentCourseSql, studentId, courseId);

            // Insert into STUDENT_GUARDIAN
            String insertStudentGuardianSql = """
                INSERT INTO public."STUDENT_GUARDIAN" (student_id, guardian_id)
                VALUES (?, ?)
                """;
            jdbcTemplate.update(insertStudentGuardianSql, studentId, guardianId);
        }
    }

    // Helper method to check if course exists
    public boolean courseExists(Long courseId) {
        String sql = "SELECT COUNT(*) FROM public.\"COURSE\" WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, courseId);
        return count != null && count > 0;
    }

    @Override
    public void deleteOrphanedStudents() {
        String sql = """
                DELETE FROM "PERSON" p
                  WHERE p.person_type_id = 2
                  AND NOT EXISTS (SELECT 1 FROM "STUDENT_COURSE" sc WHERE sc.student_id = p.id)
                  AND NOT EXISTS (SELECT 1 FROM "STUDENT_GUARDIAN" sg WHERE sg.student_id = p.id)
                  AND NOT EXISTS (SELECT 1 FROM "STUDENT_GUARDIAN" sg2 WHERE sg2.guardian_id = p.id);
            """;
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteOrphanedGuardians() {
        String sql = """
                DELETE FROM "PERSON" p
                 WHERE p.person_type_id = 3
                 AND NOT EXISTS (SELECT 1 FROM "STUDENT_GUARDIAN" sg WHERE sg.guardian_id = p.id)
                 AND NOT EXISTS (SELECT 1 FROM "STUDENT_GUARDIAN" sg2 WHERE sg2.student_id = p.id);
            """;
        jdbcTemplate.update(sql);
    }
}