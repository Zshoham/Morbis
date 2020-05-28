package com.morbis.api.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {

    private final Logger logger;

    public ApiExceptionHandler() {
        this.logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        logger.error("an unhandled exception was thrown.", exception);

        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                internalServerError);

        return new ResponseEntity<>(errorResponse, internalServerError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception) {
        logger.error("an unhandled IllegalArgumentException was thrown.", exception);

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                badRequest);

        return new ResponseEntity<>(errorResponse, badRequest);
    }
}
