package com.example.userapplication.exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = UserException.class)
    public String userAlreadyExist(UserException userException) {
        return userException.getMessage();

    }
}
