package com.micolegio.adapters.db;

import com.micolegio.domain.SchoolSupply;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
