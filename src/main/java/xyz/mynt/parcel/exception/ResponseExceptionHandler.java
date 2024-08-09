package xyz.mynt.parcel.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

/**
 * @author Julian Jupiter
 */
@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(BeanValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ProblemDetail beanValidationException(HttpServletRequest request, BeanValidationException exception) {
        if (log.isDebugEnabled()) {
            log.debug("BEAN_VALIDATION_EXCEPTION", exception);
        }

        var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        var problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail("Invalid request content.");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperties(Map.of(
                "errors", exception.getErrorDtos(),
                "createdAt", Instant.now()
        ));

        return problemDetail;
    }

    @ExceptionHandler(VoucherException.class)
    public ResponseEntity<ProblemDetail> voucherException(HttpServletRequest request, VoucherException exception) {
        if (log.isDebugEnabled()) {
            log.debug("VOUCHER_EXCEPTION", exception);
        }

        var httpStatus = exception.getHttpStatus();
        var problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperties(Map.of(
                "createdAt", Instant.now()
        ));

        return new ResponseEntity<>(problemDetail, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail otherException(HttpServletRequest request, Exception exception) {
        if (log.isDebugEnabled()) {
            log.debug("EXCEPTION", exception);
        }

        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        var problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperties(Map.of(
                "createdAt", Instant.now()
        ));

        return problemDetail;
    }
}
