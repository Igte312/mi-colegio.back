package com.micolegio.adapters.db.mappers;

import com.micolegio.domain.service.dto.response.GuardianNotificationResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class GuardianNotificationMapper {

    private GuardianNotificationMapper() {}

    public static final RowMapper<GuardianNotificationResponse> GUARDIAN_NOTIFICATION_ROW_MAPPER = new RowMapper<>() {
        @Override
        public GuardianNotificationResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            GuardianNotificationResponse response = new GuardianNotificationResponse();
            response.setStudentName(rs.getString("student_name"));
            response.setGuardianName(rs.getString("guardian_name"));
            response.setGuardianEmail(rs.getString("guardian_email"));
            return response;
        }
    };
}