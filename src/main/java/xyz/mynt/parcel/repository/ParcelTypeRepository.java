package xyz.mynt.parcel.repository;

import xyz.mynt.parcel.util.ParcelTypeCode;

import java.util.Optional;

/**
 * @author Julian Jupiter
 */
public interface ParcelTypeRepository {
    Optional<ParcelType> findByCode(ParcelTypeCode parcelTypeCode);
}
