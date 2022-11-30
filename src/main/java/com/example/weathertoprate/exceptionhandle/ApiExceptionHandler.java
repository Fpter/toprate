package com.example.weathertoprate.exceptionhandle;

import com.example.weathertoprate.exception.BusinessException;
import com.example.weathertoprate.exception.ValidateException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.text.ParseException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({ValidateException.class, ParseException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidateException(Exception ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage(), ex.getCause());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleBusinessException(Exception ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage(), ex.getCause());
    }
}
