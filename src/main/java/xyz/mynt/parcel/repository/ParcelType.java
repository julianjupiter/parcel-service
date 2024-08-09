package xyz.mynt.parcel.repository;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Julian Jupiter
 */
public record ParcelType(int id, String code, String name, BigDecimal basicCost, Instant createdAt, Instant updatedAt) {
}
