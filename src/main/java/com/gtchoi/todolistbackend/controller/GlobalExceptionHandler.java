package com.gtchoi.todolistbackend.controller;


import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public @ResponseBody ErrorResponse processUnAuthorizedException() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(UnAuthorizedException.MESSAGE);
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public @ResponseBody ErrorResponse processNPE() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("it is not your fault. it's our fault. we're going to fix this issue soon.");
        return errorResponse;
    }
}

