package xyz.mynt.parcel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Julian Jupiter
 */
public record VoucherDto(String code, BigDecimal discount, LocalDate expiry) {
}
