package com.gtchoi.todolistbackend.controller;


import com.gtchoi.todolistbackend.exception.InternalServerErrorException;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public @ResponseBody ErrorResponse processUnAuthorizedException() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(UnAuthorizedException.MESSAGE);
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { NoSuchElementException.class, EntityNotFoundException.class })
    public @ResponseBody ErrorResponse notFoundException() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("requested data not found on database.");
        return errorResponse;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NullPointerException.class, InternalServerErrorException.class})
    public @ResponseBody ErrorResponse serverError() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("it is not your fault. it's our fault. we're going to fix this issue soon.");
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody ErrorResponse jpaAssociationNotFound() {
        logger.debug("Todo를 DB에 추가할 때 Project를 찾을 수 없거나, Project를 추가할 때, User를 찾을 수 없어서 발생하는 에러.");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("can't find jpa association.");
        return errorResponse;
    }
}

