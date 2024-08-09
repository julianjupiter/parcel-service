package xyz.mynt.parcel.dto;

import jakarta.validation.constraints.NotNull;

/**
 * @author Julian Jupiter
 */
public record ParcelRequestDto(
        @NotNull(message = "${NotNull.parcelRequestDto.weight}")
        Double weight,
        @NotNull(message = "${NotNull.parcelRequestDto.height}")
        Double height,
        @NotNull(message = "${NotNull.parcelRequestDto.width}")
        Double width,
        @NotNull(message = "${NotNull.parcelRequestDto.length}")
        Double length,
        String voucherCode) {
}
