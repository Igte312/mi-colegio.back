package com.micolegio.adapters.db.mappers;

import com.micolegio.domain.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import static com.micolegio.adapters.db.constants.CourseFields.*;

public final class CourseEntityMapper {

    private CourseEntityMapper() {}

    public static final RowMapper<Course> COURSE_ROW_MAPPER = new RowMapper<>() {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course();
            course.setId(rs.getLong(ID));
            course.setName(rs.getString(NAME));
            course.setLevel(rs.getString(LEVEL));
            course.setLetter(rs.getString(LETTER));
            course.setSchoolId(rs.getLong(SCHOOL_ID));
            course.setHomeroomTeacherId(rs.getLong(HOMEROOM_TEACHER_ID));
            course.setCreatedAt(rs.getTimestamp(CREATED_AT).toLocalDateTime());
            return course;
        }
    };
}
