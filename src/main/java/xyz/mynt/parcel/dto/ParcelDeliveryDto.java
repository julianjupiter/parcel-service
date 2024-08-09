package xyz.mynt.parcel.dto;

import xyz.mynt.parcel.util.ParcelTypeCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Julian Jupiter
 */
public record ParcelDeliveryDto(
        ParcelTypeCode type,
        BigDecimal cost,
        String voucherCode,
        BigDecimal discount) {

    public static ParcelDeliveryDto of(ParcelTypeCode type, BigDecimal cost) {
        return new ParcelDeliveryDto(type, cost, null, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    public ParcelDeliveryDto withDiscount(String voucherCode, BigDecimal discount) {
        return new ParcelDeliveryDto(this.type, this.cost, voucherCode, discount);
    }
}
