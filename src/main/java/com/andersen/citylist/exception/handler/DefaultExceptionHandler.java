package com.andersen.citylist.exception.handler;

import com.andersen.citylist.dto.ApiErrorViewModel;
import com.andersen.citylist.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorViewModel processNotFoundException(final NotFoundException exception) {
        log.error("Default ExceptionHandler:: exception: {}", exception.getMessage(), exception);

        return ApiErrorViewModel.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
    }
}
