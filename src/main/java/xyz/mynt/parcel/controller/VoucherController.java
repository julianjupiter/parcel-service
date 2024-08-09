package xyz.mynt.parcel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.mynt.parcel.dto.VoucherDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This is temporary only to test voucher API in local
 * since the mock server of Voucher API has Gateway Timeout error.
 * Use Mynt Custom API Base URL: http://localhost:8080/parcel-service/api
 * See application.yml: mynt-custom-api.base-url
 * @author Julian Jupiter
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {
    private static final Logger log = LoggerFactory.getLogger(VoucherController.class);

    @GetMapping("/{voucherCode}")
    public ResponseEntity<Object> voucher(@PathVariable String voucherCode, @RequestParam("key") String apiKey) {
        log.info("voucherCode: {}", voucherCode);
        log.info("apiKey: {}", apiKey);
        return vouchers().stream()
                .filter(voucherDto -> voucherDto.code().equals(voucherCode))
                .findFirst()
                .<ResponseEntity<Object>>map(voucherDto -> new ResponseEntity<>(voucherDto, HttpStatusCode.valueOf(200)))
                .orElseGet(() -> new ResponseEntity<>(
                        Map.of("error", "Invalid voucher code"),
                        HttpStatusCode.valueOf(400)
                ));

    }

    private static List<VoucherDto> vouchers() {
        return List.of(
                new VoucherDto("MYNT", BigDecimal.valueOf(10.0), LocalDate.now().plusDays(3)),
                new VoucherDto("GFI", BigDecimal.valueOf(15.0), LocalDate.now()),
                new VoucherDto("skdlds", BigDecimal.valueOf(15.0), LocalDate.now().minusDays(2))
        );
    }
}
