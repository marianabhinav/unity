package com.amcrest.unity.accounting.exceptions;

import com.amcrest.unity.accounting.email.EmailServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class ErrorHandlingControllerAdvice{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onJsonParseException(
            JsonParseException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        String message = e.getMessage();
            error.getViolations().add(
                    new Violation(
                            message.substring(0, message.indexOf(':')),
                            e.getMessage().substring(e.getMessage().indexOf(':')+1, e.getMessage().indexOf('\n'))
                    )
            );
        return error;
    }


}
