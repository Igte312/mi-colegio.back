package com.micolegio.adapters.db;

import com.micolegio.domain.CourseSupplyList;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.micolegio.adapters.db.mappers.CourseSupplyListMapper.COURSE_SUPPLY_LIST_ROW_MAPPER;

@Repository
@AllArgsConstructor
public class CourseSupplyListRepository implements ICourseSupplyListRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void saveAll(List<CourseSupplyList> courseSupplyLists) {
        String sql = """
                INSERT INTO public."COURSE_SUPPLY_LIST" (school_supply_id, course_id, is_active, quantity)
                VALUES (?, ?, ?, ?)
                """;

        for (CourseSupplyList item : courseSupplyLists) {
            jdbcTemplate.update(sql,
                item.getSchoolSupplyId(),
                item.getCourseId(),
                item.getIsActive(),
                item.getQuantity());
        }
    }

    @Override
    public List<CourseSupplyList> findByCourseId(Long courseId) {
        String sql = """
                SELECT id, school_supply_id, course_id, is_active, quantity
                FROM public."COURSE_SUPPLY_LIST"
                WHERE course_id = ?
                """;

        return jdbcTemplate.query(sql, COURSE_SUPPLY_LIST_ROW_MAPPER, courseId);
    }

    @Override
    public void updateIsActiveByCourseIdAndSchoolSupplyIds(Long courseId, List<Long> schoolSupplyIds, boolean isActive) {
        if (schoolSupplyIds.isEmpty()) {
            return;
        }

        String placeholders = String.join(",", schoolSupplyIds.stream().map(id -> "?").toArray(String[]::new));

        String sql = String.format("""
                UPDATE public."COURSE_SUPPLY_LIST"
                SET is_active = ?
                WHERE course_id = ? AND school_supply_id IN (%s)
                """, placeholders);

        Object[] params = new Object[schoolSupplyIds.size() + 2];
        params[0] = isActive;
        params[1] = courseId;
        for (int i = 0; i < schoolSupplyIds.size(); i++) {
            params[i + 2] = schoolSupplyIds.get(i);
        }

        jdbcTemplate.update(sql, params);
    }

    @Override
    public boolean existsBySchoolSupplyId(Long schoolSupplyId) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM public."SCHOOL_SUPPLY"
                WHERE id = ?
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, schoolSupplyId));
    }
}