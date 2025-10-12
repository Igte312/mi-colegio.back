package com.micolegio.adapters.db;

import com.micolegio.domain.SchoolSupply;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.micolegio.adapters.db.mappers.CourseSupplyMapper.COURSE_SUPPLY_ROW_MAPPER;
import static com.micolegio.adapters.db.mappers.SupplyEntityMapper.SUPPLY_ROW_MAPPER;

@Repository
@AllArgsConstructor
public class SchoolSupplyRepository implements ISchoolSupplyRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SchoolSupply> getGeneralActiveSupplies() {
        String sql = """
                SELECT id, "name", description, is_active
                FROM public."SCHOOL_SUPPLY"
                WHERE is_active = true;
                """;
        return jdbcTemplate.query(sql, SUPPLY_ROW_MAPPER);
    }

    @Override
    public List<CourseSupplyResponse> getSuppliesByCourseId(Long schoolId, Long courseId) {
        String sql = """
                SELECT
                      school_supply_id,
                      course_id,
                      ss."name",
                      ss.description,
                      cs.quantity
                  FROM public."COURSE_SUPPLY_LIST" cs
                      left JOIN public."COURSE" c\s
                        on cs.course_id = c.id
                    left join public."SCHOOL_SUPPLY" ss\s
                        on ss.id = cs.school_supply_id\s
                   where c.school_id = ?
                    and cs.course_id = ?
                    and cs.is_active = true;
                """;
        return jdbcTemplate.query(sql, COURSE_SUPPLY_ROW_MAPPER, schoolId, courseId);
    }
}
