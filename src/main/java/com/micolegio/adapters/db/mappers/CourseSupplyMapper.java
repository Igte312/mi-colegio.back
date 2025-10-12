package com.micolegio.adapters.db.mappers;

import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CourseSupplyMapper {

    private CourseSupplyMapper() {}

    public static final RowMapper<CourseSupplyResponse> COURSE_SUPPLY_ROW_MAPPER = new RowMapper<>() {
        @Override
        public CourseSupplyResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            CourseSupplyResponse response = new CourseSupplyResponse();
            response.setSchoolSupplyId(rs.getLong("school_supply_id"));
            response.setCourseId(rs.getLong("course_id"));
            response.setName(rs.getString("name"));
            response.setDescription(rs.getString("description"));
            response.setQuantity(rs.getInt("quantity"));
            return response;
        }
    };
}
