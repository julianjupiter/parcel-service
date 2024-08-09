package xyz.mynt.parcel.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Julian Jupiter
 */
@Component
class ParcelTypeRowMapper implements RowMapper<ParcelType> {
    @Override
    public ParcelType mapRow(ResultSet rs, int rowNum) throws SQLException {
        var createdAt = rs.getTimestamp("created_at");
        var updatedAt = rs.getTimestamp("updated_at");

        return new ParcelType(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getBigDecimal("basic_cost"),
                createdAt != null ? createdAt.toInstant() : null,
                updatedAt != null ? updatedAt.toInstant() : null
        );
    }
}
