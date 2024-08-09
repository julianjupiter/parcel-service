package xyz.mynt.parcel.service;

import xyz.mynt.parcel.dto.ParcelDeliveryDto;
import xyz.mynt.parcel.dto.ParcelRequestDto;

/**
 * @author Julian Jupiter
 */
public interface ParcelService {
    ParcelDeliveryDto calculate(ParcelRequestDto parcelRequestDto);
}
