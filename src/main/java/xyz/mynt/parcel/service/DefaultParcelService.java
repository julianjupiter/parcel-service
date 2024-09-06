package xyz.mynt.parcel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpClientErrorException;
import xyz.mynt.parcel.config.ParcelRulesConfig;
import xyz.mynt.parcel.dto.ParcelDeliveryDto;
import xyz.mynt.parcel.dto.ParcelRequestDto;
import xyz.mynt.parcel.dto.VoucherDto;
import xyz.mynt.parcel.exception.BeanValidationException;
import xyz.mynt.parcel.exception.VoucherException;
import xyz.mynt.parcel.repository.ParcelType;
import xyz.mynt.parcel.repository.ParcelTypeRepository;
import xyz.mynt.parcel.util.BeanValidator;
import xyz.mynt.parcel.util.ParcelTypeCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Julian Jupiter
 */
@Service
@Transactional
class DefaultParcelService implements ParcelService {
    private static final Logger log = LoggerFactory.getLogger(DefaultParcelService.class);
    private final MessageSource messageSource;
    private final Validator validator;
    private final ParcelRulesConfig parcelRulesConfig;
    private final ParcelTypeRepository parcelTypeRepository;
    private final VoucherService voucherService;
    private ParcelTypeCode parcelTypeCode;

    DefaultParcelService(
            MessageSource messageSource,
            Validator validator,
            ParcelRulesConfig parcelRulesConfig,
            ParcelTypeRepository parcelTypeRepository,
            VoucherService voucherService) {
        this.messageSource = messageSource;
        this.validator = validator;
        this.parcelRulesConfig = parcelRulesConfig;
        this.parcelTypeRepository = parcelTypeRepository;
        this.voucherService = voucherService;
    }

    @Override
    public ParcelDeliveryDto calculate(ParcelRequestDto parcelRequestDto) {
        this.validateRequest(parcelRequestDto);
        BigDecimal cost = this.calculateCost(parcelRequestDto);
        var parcelDeliveryDto = ParcelDeliveryDto.of(parcelTypeCode, cost);

        if (ParcelTypeCode.REJECT != this.parcelTypeCode) {
            String voucherCode = parcelRequestDto.voucherCode();
            if (voucherCode != null && !voucherCode.isBlank()) {
                VoucherDto voucherDto = this.applyDiscount(voucherCode);
                BigDecimal discount = voucherDto.discount()
                        .setScale(2, RoundingMode.HALF_UP);
                log.info("Discount for voucher code {}: {}", voucherCode, discount);
                return parcelDeliveryDto.withDiscount(voucherCode, discount);
            }
        }

        return parcelDeliveryDto;
    }

    private void validateRequest(ParcelRequestDto parcelRequestDto) {
        var bindingResult = BeanValidator.validate(this.validator, parcelRequestDto, "parcelRequestDto");
        if (bindingResult.hasErrors()) {
            var errors = BeanValidator.extractErrors(bindingResult, this.messageSource);
            throw new BeanValidationException(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), errors);
        }
    }

    private BigDecimal calculateCost(ParcelRequestDto parcelRequestDto) {
        var weight = parcelRequestDto.weight();
        log.info("Weight: {}", weight);
        BigDecimal cost;
        if (weight > this.parcelRulesConfig.getReject()) {
            this.parcelTypeCode = ParcelTypeCode.REJECT;
            cost = BigDecimal.ZERO;
        } else if (weight > this.parcelRulesConfig.getHeavyParcel()) {
            this.parcelTypeCode = ParcelTypeCode.HEAVY_PARCEL;
            cost = this.parcelTypeRepository.findByCode(this.parcelTypeCode)
                    .map(ParcelType::basicCost)
                    .map(BigDecimal.valueOf(weight)::multiply)
                    .orElse(BigDecimal.ZERO);
        } else {
            var volume = this.computeVolume(parcelRequestDto);
            log.info("Volume: {}", volume);
            var volumeDecimal = BigDecimal.valueOf(volume);
            if (volume < this.parcelRulesConfig.getSmallParcel()) {
                this.parcelTypeCode = ParcelTypeCode.SMALL_PARCEL;
            } else if (volume < this.parcelRulesConfig.getMediumParcel()) {
                this.parcelTypeCode = ParcelTypeCode.MEDIUM_PARCEL;
            } else {
                this.parcelTypeCode = ParcelTypeCode.LARGE_PARCEL;
            }

            cost = this.parcelTypeRepository.findByCode(this.parcelTypeCode)
                    .map(ParcelType::basicCost)
                    .map(volumeDecimal::multiply)
                    .orElse(BigDecimal.ZERO);
        }

        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    private VoucherDto applyDiscount(String voucherCode) {
        return this.getVoucher(voucherCode)
                .filter(voucherDto -> {
                    var today = LocalDate.now();
                    var expiry = voucherDto.expiry();
                    return today.isEqual(expiry) || today.isBefore(expiry);
                })
                .orElseThrow(() -> new VoucherException("Expired voucher code", HttpStatus.UNPROCESSABLE_ENTITY));
    }

    private double computeVolume(ParcelRequestDto parcelRequestDto) {
        var height = parcelRequestDto.height();
        var width = parcelRequestDto.width();
        var length = parcelRequestDto.length();
        log.info("Height: {}", height);
        log.info("Width: {}", width);
        log.info("Length: {}", length);

        return height * width * length;
    }

    private Optional<VoucherDto> getVoucher(String voucherCode) {
        var logMessage = "Error on request for voucher: {}";
        try {
            return this.voucherService.getVoucher(voucherCode);
        } catch (HttpClientErrorException e) {
            log.info(logMessage, e.getMessage());
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new VoucherException("Invalid voucher code", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e) {
            log.info(logMessage, e.getMessage());
        }

        throw new VoucherException("Error on request for voucher", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
