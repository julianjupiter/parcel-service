package xyz.mynt.parcel.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.mynt.parcel.dto.DataResponseDto;
import xyz.mynt.parcel.dto.ParcelRequestDto;
import xyz.mynt.parcel.service.ParcelService;

import java.net.URI;
import java.time.Instant;

/**
 * @author Julian Jupiter
 */
@RestController
@RequestMapping("/v1/parcels")
public class ParcelController {
    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @PostMapping("/delivery-cost")
    public DataResponseDto calculate(HttpServletRequest request, @RequestBody ParcelRequestDto parcelRequestDto) {
        var deliveryCostDto = this.parcelService.calculate(parcelRequestDto);
        var httpStatus = HttpStatus.OK;
        return new DataResponseDto(
                httpStatus.getReasonPhrase(),
                httpStatus.value(),
                deliveryCostDto,
                URI.create(request.getRequestURI()),
                Instant.now()
        );
    }
}
