package com.micolegio.adapters.db;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<UserBasicInfo> userBasicInfoRowMapper = new RowMapper<UserBasicInfo>() {
        @Override
        public UserBasicInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserBasicInfo(
                    rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getBoolean("is_active"),
                    rs.getString("role_name"),
                    rs.getLong("school_id"),
                    rs.getString("school_name")
            );
        }
    };

    @Override
    public UserBasicInfo getUserBasicInfoByEmail(String email) {
        String sql = """
            SELECT u.id,
                u.first_name,
                u.last_name,
                u.email,
                u.is_active,
                r.name AS role_name,
                u.school_id,
                s.name AS school_name
            FROM USER_APP u
                JOIN ROLE r ON u.role_id = r.id
                JOIN SCHOOL s ON s.id = u.school_id
            WHERE u.email = ?
                AND u.is_active = 1
        """;

        return jdbcTemplate.queryForObject(sql, userBasicInfoRowMapper, email);
    }
}
