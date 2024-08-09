package xyz.mynt.parcel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import xyz.mynt.parcel.client.VoucherClient;
import xyz.mynt.parcel.dto.VoucherDto;

import java.util.Optional;

/**
 * @author Julian Jupiter
 */
@Service
class DefaultVoucherService implements VoucherService {
    private static final Logger log = LoggerFactory.getLogger(DefaultVoucherService.class);
    private final VoucherClient voucherClient;
    private final String voucherClientApiKey;

    DefaultVoucherService(VoucherClient voucherClient, @Value("${mynt-custom-api.api-key}") String voucherClientApiKey) {
        this.voucherClient = voucherClient;
        this.voucherClientApiKey = voucherClientApiKey;
    }

    @Override
    public Optional<VoucherDto> getVoucher(String code) {
        RetryContext retryContext = RetrySynchronizationManager.getContext();
        if (retryContext != null) {
            log.info("Retry: {}", (retryContext.getRetryCount() + 1));
        } else {
            log.warn("RetryContext is null");
        }

        ResponseEntity<VoucherDto> response = this.voucherClient.getVoucher(code, this.voucherClientApiKey);
        var httpStatusCode = response.getStatusCode();
        if (httpStatusCode == HttpStatus.OK) {
            return Optional.ofNullable(response.getBody());
        }

        return Optional.empty();
    }
}
