package xyz.mynt.parcel.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import xyz.mynt.parcel.dto.VoucherDto;

import java.util.Optional;

/**
 * @author Julian Jupiter
 */
public interface VoucherService {
    @Retryable(
            retryFor = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000)
    )
    Optional<VoucherDto> getVoucher(String code);
}
