package xyz.mynt.parcel.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import xyz.mynt.parcel.dto.VoucherDto;

/**
 * @author Julian Jupiter
 */
@HttpExchange(accept = "application/json")
public interface VoucherClient {
    @GetExchange("/voucher/{voucherCode}")
    ResponseEntity<VoucherDto> getVoucher(
            @PathVariable
            String voucherCode,
            @RequestParam("key")
            String apiKey
    );
}
