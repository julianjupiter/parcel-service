package xyz.mynt.parcel.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import xyz.mynt.parcel.util.ParcelTypeCode;

import java.util.Optional;

/**
 * @author Julian Jupiter
 */
@Repository
class DefaultParcelTypeRepository implements ParcelTypeRepository {
    private static final String CODE = "code";
    private final JdbcClient jdbcClient;
    private final ParcelTypeRowMapper parcelTypeRowMapper;

    DefaultParcelTypeRepository(JdbcClient jdbcClient, ParcelTypeRowMapper parcelTypeRowMapper) {
        this.jdbcClient = jdbcClient;
        this.parcelTypeRowMapper = parcelTypeRowMapper;
    }

    @Override
    public Optional<ParcelType> findByCode(ParcelTypeCode parcelTypeCode) {
        return this.jdbcClient.sql(ParcelTypeQuery.SELECT_BY_CODE)
                .param(CODE, parcelTypeCode.name())
                .query(this.parcelTypeRowMapper)
                .optional();
    }
}
