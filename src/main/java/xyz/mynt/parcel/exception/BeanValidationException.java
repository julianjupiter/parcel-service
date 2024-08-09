package xyz.mynt.parcel.exception;

import xyz.mynt.parcel.dto.ErrorDto;

import java.util.List;

/**
 * @author Julian Jupiter
 */
public class BeanValidationException extends RuntimeException {
    private final List<ErrorDto> errorDtos;

    public BeanValidationException(String message, List<ErrorDto> errorDtos) {
        super(message);
        this.errorDtos = errorDtos;
    }

    public List<ErrorDto> getErrorDtos() {
        return errorDtos;
    }
}
