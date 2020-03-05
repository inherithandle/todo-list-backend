package com.gtchoi.todolistbackend.controller;


import com.gtchoi.todolistbackend.exception.InternalServerErrorException;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.model.ErrorResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
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
    @ExceptionHandler(HttpClientErrorException.class)
    public @ResponseBody ErrorResponse processHttpClientBadRequestException(Exception e) {
        HttpClientErrorException ex = (HttpClientErrorException) e;
        logger.debug("status text: {}, message: {}", ex.getStatusText(), ex.getMessage());
        logger.debug("response body: {}", ex.getResponseBodyAsString());
        logger.debug("stack trace: {}", ex.getStackTrace());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { NoSuchElementException.class, EntityNotFoundException.class})
    public @ResponseBody ErrorResponse notFoundException() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("requested data not found on database.");
        return errorResponse;
    }

    /**
     * 유저가 입력한 폼 데이터가 유효하지 않음.
     * 프론트단에서 체크해야 했으나, 프론트 개발자의 잘못으로 유저의 잘못된 데이터가 Backend로 넘어왔을 때 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public @ResponseBody ErrorResponse serverValidationException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("data is not valid.");
        return errorResponse;
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NullPointerException.class, InternalServerErrorException.class})
    public @ResponseBody ErrorResponse serverError(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("it is not your fault. it's our fault. we're going to fix this issue soon.");
        String stackTrace = ExceptionUtils.getStackTrace(e);
        logger.error(stackTrace);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ErrorResponse validatorFindsError(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        ObjectError error = e.getBindingResult().getAllErrors().get(0);
        errorResponse.setMessage(error.getDefaultMessage());
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public @ResponseBody ErrorResponse headerNeeded() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Content-Type:application/json;charset=UTF-8 헤더가 필요합니다.");
        return errorResponse;
    }
}

