package xyz.mynt.parcel.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Julian Jupiter
 */
public class VoucherException extends RuntimeException {
    private final HttpStatus httpStatus;

    public VoucherException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
