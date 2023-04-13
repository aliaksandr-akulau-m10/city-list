package com.andersen.citylist.exception.handler;

import com.andersen.citylist.dto.ApiErrorViewModel;
import com.andersen.citylist.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorViewModel processNotFoundException(final NotFoundException exception) {
        log.error("Default ExceptionHandler::processNotFoundException", exception);

        return ApiErrorViewModel.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorViewModel processValidationException(final MethodArgumentNotValidException exception) {
        log.error("Default ExceptionHandler::processValidationException", exception);

        final List<String> errorMessages = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();

        return ApiErrorViewModel.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessages.toString())
                .build();
    }
}
