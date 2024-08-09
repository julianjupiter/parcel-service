package xyz.mynt.parcel.repository;

/**
 * @author Julian Jupiter
 */
class ParcelTypeQuery {
    public static final String SELECT_BY_CODE = """
            SELECT id, code, name, basic_cost, created_at, updated_at
            FROM parcel_type
            WHERE code = :code
            """;

    private ParcelTypeQuery() {
    }
}
