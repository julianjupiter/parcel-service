package xyz.mynt.parcel.util;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import xyz.mynt.parcel.dto.ErrorDto;

import java.util.List;
import java.util.Locale;

/**
 * @author Julian Jupiter
 */
public class BeanValidator {
    private BeanValidator() {
    }

    public static BindingResult validate(Validator validator, Object target, String objectName) {
        var dataBinder = new DataBinder(target, objectName);
        dataBinder.addValidators(validator);
        dataBinder.validate();

        return dataBinder.getBindingResult();
    }

    public static List<ErrorDto> extractErrors(BindingResult bindingResult, MessageSource messageSource) {
        var fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(fieldError -> {
                    var fieldErrorCode = fieldError.getCode();
                    var field = fieldError.getField();
                    var resolveMessageCodes = bindingResult.resolveMessageCodes(fieldErrorCode);
                    var code = resolveMessageCodes[0] + "." + field;
                    var message = messageSource.getMessage(code, new Object[]{fieldError.getRejectedValue()}, Locale.ENGLISH);
                    return new ErrorDto(message);
                })
                .toList();
    }
}
