package com.micolegio.adapters.db.mappers;

import com.micolegio.domain.CourseSupplyList;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CourseSupplyListMapper {

    private CourseSupplyListMapper() {}

    public static final RowMapper<CourseSupplyList> COURSE_SUPPLY_LIST_ROW_MAPPER = new RowMapper<>() {
        @Override
        public CourseSupplyList mapRow(ResultSet rs, int rowNum) throws SQLException {
            CourseSupplyList courseSupplyList = new CourseSupplyList();
            courseSupplyList.setId(rs.getLong("id"));
            courseSupplyList.setSchoolSupplyId(rs.getLong("school_supply_id"));
            courseSupplyList.setCourseId(rs.getLong("course_id"));
            courseSupplyList.setIsActive(rs.getBoolean("is_active"));
            courseSupplyList.setQuantity(rs.getInt("quantity"));
            return courseSupplyList;
        }
    };
}