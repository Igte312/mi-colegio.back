package com.micolegio.adapters.db;

import com.micolegio.domain.Course;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.micolegio.adapters.db.mappers.CourseEntityMapper.COURSE_ROW_MAPPER;

@Repository
@AllArgsConstructor
public class CourseRepository implements ICourseRepository{

    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Course> findBySchoolId(long schoolId) {
        String sql = """
                SELECT
                    id,
                    name,
                    "level",                -- Se usa comillas dobles para la palabra reservada
                    letter,
                    school_id,
                    homeroom_teacher_id,
                    created_at
                FROM public."COURSE"        -- Se usa comillas dobles y MAYÚSCULAS
                WHERE school_id = ?
                """;

        return jdbcTemplate.query(sql, COURSE_ROW_MAPPER, schoolId);
    }
}
