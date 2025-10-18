package com.micolegio.adapters.db;

import com.micolegio.domain.service.dto.response.GuardianNotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.micolegio.adapters.db.mappers.GuardianNotificationMapper.GUARDIAN_NOTIFICATION_ROW_MAPPER;

@Repository
@AllArgsConstructor
public class GuardianNotificationRepository implements IGuardianNotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<GuardianNotificationResponse> findGuardiansByCourseId(Long courseId) {
        String sql = """
                SELECT
                    CONCAT(s.first_name, ' ', s.last_name) as student_name,
                    CONCAT(g.first_name, ' ', g.last_name) as guardian_name,
                    g.email as guardian_email
                FROM public."STUDENT_COURSE" sc
                JOIN public."PERSON" s ON sc.student_id = s.id
                JOIN public."STUDENT_GUARDIAN" sg ON s.id = sg.student_id
                JOIN public."PERSON" g ON sg.guardian_id = g.id
                WHERE sc.course_id = ?
                AND s.is_active = true
                AND g.is_active = true
                """;

        return jdbcTemplate.query(sql, GUARDIAN_NOTIFICATION_ROW_MAPPER, courseId);
    }
}