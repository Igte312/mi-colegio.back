package com.micolegio.adapters.db.mappers;

import com.micolegio.domain.SchoolSupply;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SupplyEntityMapper {

    private SupplyEntityMapper() {}

    public static final RowMapper<SchoolSupply> SUPPLY_ROW_MAPPER = new RowMapper<>() {
        @Override
        public SchoolSupply mapRow(ResultSet rs, int rowNum) throws SQLException {
            SchoolSupply s = new SchoolSupply();
            s.setId(rs.getLong("id"));
            s.setName(rs.getString("name"));
            s.setDescription(rs.getString("description"));
            s.setActive(rs.getBoolean("is_active"));
            return s;
        }
    };
}
